from urllib.request import urlopen, Request
from bs4 import BeautifulSoup
import sys

def main():
    
    url = sys.argv[1]
    fileName = sys.argv[2]
    webPage = urlopen(Request(url, headers={'User-Agent': 'Chrome'}))
    file = open(fileName, "wb")
    file.write(webPage.read())
    file.close()
       
    print(".......................")
main()