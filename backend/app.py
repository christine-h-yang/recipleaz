from flask import Flask
from flask import request
from selenium import webdriver
from selenium.webdriver.common.desired_capabilities import DesiredCapabilities
from pdb import set_trace as bp
import re
from threading import Lock

app = Flask(__name__)
lock = Lock()

def get_price_estimate(search_text):
    found_prices = []
    with lock:
        driver = webdriver.Remote(
           command_executor='http://127.0.0.1:4444/wd/hub',
           desired_capabilities=DesiredCapabilities.CHROME)

        driver.get("http://google.com")

        search_box = driver.find_element_by_id('lst-ib')
        search_box.send_keys("price of " + search_text);
        search_box.submit()

        result_text = ' '.join([stuff.text for stuff in driver.find_elements_by_class_name('st')])
        found_prices = re.findall(r"[$]\d+\.\d+", result_text)
        driver.quit()

    if len(found_prices) > 0:
        found_prices.sort()
        price = found_prices[len(found_prices)//2]
        return "{'estimate': %s}" % price
    else:
        return "{'estimate': %s}" % 0.00

@app.route("/")
def get_estimate():
    search_text = request.args.get('q')
    return get_price_estimate(search_text)
