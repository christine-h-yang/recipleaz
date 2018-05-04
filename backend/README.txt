# Set-up
```
pip install Flask
FLASK_APP=app.py flask run
```

Run the selenium server:
```
docker run -d -p 4444:4444 -v /dev/shm:/dev/shm selenium/standalone-chrome:3.11.0-dysprosium
```
