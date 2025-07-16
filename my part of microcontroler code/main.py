import network
from time import sleep
import random
from machine import Pin
import ufirebase as firebase
import urequests
import ujson


ssid = "ssid" #Your network name
password = "password" #Your WiFi password

led_onboard = Pin("LED", Pin.OUT)

#Connect to WLAN
wlan = network.WLAN(network.STA_IF)
wlan.active(True)
wlan.connect(ssid, password)
while wlan.isconnected() == False:
    led_onboard.value(0)
    print('Connecting...')
    sleep(1)
ip = wlan.ifconfig()[0]
print('Connection successful')
print(f'Connected on {ip} \n')
led_onboard.value(1)
url = "https://oauth2.googleapis.com/token"
fcm_url = "https://fcm.googleapis.com/v1/projects/projects-name/messages:send"
# Create the connection to our Firebase database - don't forget to change the URL!
firebase.setURL("https://projekt-zespolowy-zamrazarka-default-rtdb.europe-west1.firebasedatabase.app/")
payload = {'refresh_token': 'refresh_token',
           'client_id': 'client_id',
           'client_secret': 'client_secret',
           'redirect_uri': 'redirect_uri',
           'grant_type': 'refresh_token'}

refresh_headers = {}
fcm_payload = {
    "message": {
        "topic": "global",
        "notification": {
            "title": "123",
            "body": "123"
        }
    }
}

fcm_headers = {
  'Content-Type': 'text/plain',
  'Authorization': 'Bearer {BEARER}'
}
BEARER=""
while True:
    try:
    # Measure temperature
    
    # Query sensors
        temp_C = random.randint(20, 50)
        print('Temp_C :', temp_C , '°C')
        firebase.put("Temp_C", temp_C, bg=0)
        
        if temp_C>21 :
            fcm_headers['Authorization'] = 'Bearer ' + BEARER
            fcm_response = urequests.post(fcm_url, headers=fcm_headers, data=ujson.dumps(fcm_payload))
            print(fcm_response.text)
            x = ujson.loads(fcm_response.text)
            
            if "error" in fcm_response.text:
                response = urequests.post(url, json=payload)
                print(response.text)
                y = ujson.loads(response.text)
                BEARER=y["access_token"]
    except OSError as e:
        #continue
        print('Connection closed')