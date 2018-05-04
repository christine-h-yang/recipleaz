package cs371m.com.recipleaz;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.api.ClarifaiResponse;
import clarifai2.dto.input.ClarifaiImage;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.model.ConceptModel;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;
import okhttp3.OkHttpClient;

public class ClarifaiService {

    public interface ProcessClarifaiConcepts {
        public void onPostExecute(List<String> concepts);
    }

    private static final String API_KEY  = "ec5f6c922de844428c4678e7e171adab";

    private static ClarifaiService INSTANCE;

    public static ClarifaiService getInstance() {
        if (INSTANCE == null) {
            new ClarifaiService().init();
        }
        final ClarifaiService instance = INSTANCE;

        return instance;
    }

    @Nullable
    private ClarifaiClient client;

    public void init() {
        INSTANCE = this;
        client = new ClarifaiBuilder(API_KEY)
                // Optionally customize HTTP client via a custom OkHttp instance
                .client(new OkHttpClient.Builder()
                        .readTimeout(30, TimeUnit.SECONDS) // Increase timeout for poor mobile networks
                        .build()
                )
                .buildSync(); // use build() instead to get a Future<ClarifaiClient>, if you don't want to block this thread
    }

    @NonNull
    private ClarifaiClient clarifaiClient() {
        final ClarifaiClient client = this.client;
        if (client == null) {
            throw new IllegalStateException("Cannot use Clarifai client before initialized");
        }
        return client;
    }

    /**
     * @param context
     * @param data
     * @return
     */
    @Nullable
    public static byte[] retrieveSelectedImage(@NonNull Context context, @NonNull Intent data) {
        InputStream inStream = null;
        Bitmap bitmap = null;
        try {
            inStream = context.getContentResolver().openInputStream(data.getData());
            bitmap = BitmapFactory.decodeStream(inStream);
            final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            return outStream.toByteArray();
        } catch (FileNotFoundException e) {
            return null;
        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException ignored) {
                }
            }
            if (bitmap != null) {
                bitmap.recycle();
            }
        }
    }

    public void processImage(String url) {
        new AsyncProcessImage(url).execute();
    }

    public void processImage(byte[] imageBytes, ProcessClarifaiConcepts onPostExecute) {
        new AsyncProcessImage(imageBytes, onPostExecute).execute();
        /*
        final byte[] imageBytes = ClarifaiUtil.retrieveSelectedImage(this, data);
        if (imageBytes != null) {
            onImagePicked(imageBytes);
        }
        */
    }

    private static class AsyncProcessImage extends AsyncTask<Void, Void, ClarifaiResponse<List<ClarifaiOutput<Concept>>>> {

        String url;
        byte[] imageBytes;
        boolean isURL = false;
        ProcessClarifaiConcepts conceptHandler;

        AsyncProcessImage() {
            throw new IllegalStateException("Cannot create an AsyncProcessImage with default constructor");
        }

        AsyncProcessImage(String url) {
            this.url = url;
            isURL = true;
        }

        AsyncProcessImage(byte[] imageBytes, ProcessClarifaiConcepts conceptHandler) {
            this.imageBytes = imageBytes;
            isURL = false;
            this.conceptHandler = conceptHandler;
        }

        @Override protected ClarifaiResponse<List<ClarifaiOutput<Concept>>> doInBackground(Void... params) {
            // The default Clarifai model that identifies concepts in images
            final ConceptModel generalModel = ClarifaiService.getInstance().clarifaiClient().getDefaultModels().generalModel();

            ClarifaiImage image = isURL
                    ? ClarifaiImage.of(url)
                    : ClarifaiImage.of(imageBytes);

            // Use this model to predict, with the image that the user just selected as the input
            return generalModel.predict()
                    .withInputs(ClarifaiInput.forImage(image))
                    .executeSync();
        }

        @Override protected void onPostExecute(ClarifaiResponse<List<ClarifaiOutput<Concept>>> response) {
            if (!response.isSuccessful()) {
                Log.d("ERROR", "processImage: ERROR WHEN CONTACTING CLARIFAI API");
                return;
            }
            final List<ClarifaiOutput<Concept>> predictions = response.get();
            if (predictions.isEmpty()) {
                Log.d("NO RESULTS", "processImage: NO RESULTS FROM CLARIFAI API");
                return;
            }

            Log.d("DATA", "onPostExecute: " + predictions.get(0).data());

            if (conceptHandler != null) {
                List<Concept> output = predictions.get(0).data();
                List<String> simplePredictions = new ArrayList<>();
                double minConfidence = 0.90;
                for (Concept concept : output) {
                    if (concept.value() >= minConfidence) {
                        simplePredictions.add(concept.name());
                    }
                }
                conceptHandler.onPostExecute(simplePredictions);
            }
        }
    }
}
