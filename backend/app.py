from flask import Flask
from selenium import webdriver
from selenium.webdriver.common.desired_capabilities import DesiredCapabilities
from pdb import set_trace as bp
import re

app = Flask(__name__)

def get_price_estimate(search_text):
    driver = webdriver.Remote(
       command_executor='http://127.0.0.1:4444/wd/hub',
       desired_capabilities=DesiredCapabilities.CHROME)

    driver.get("http://google.com")

    search_box = driver.find_element_by_id('lst-ib')
    search_box.send_keys("price of " + search_text);
    search_box.submit()

    result_text = ' '.join([stuff.text for stuff in driver.find_elements_by_class_name('st')])
    found_prices = re.findall(r"\$[^\ ]+", result_text)

    #bp()
    if len(found_prices) > 0:
        price = found_prices[0]
        return "Price estimate: %s" % price
    else:
        return "Could not estimate price."

@app.route("/")
def get_estimate():
    search_text = "chicken soup"
    return get_price_estimate(search_text)
