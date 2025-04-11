
# Getting Started

### Setup instructions

The application will take the api key from an environment varible, 
so the first step is to set your api key.
```
set API_KEY=your-secret-api-key

e.g 

set API_KEY=QSo5GRADl0AWaii20X7NzeqoL_Vkuo25
```

To run the application from command line execute the following :
```
mvn spring-boot:run
```
### Data base access

Ensure that the data base is created in MySql

```
data base name = stocks_db
```
To configure access to MySql set user name and password in file 
application.properties under src/main/java/resources
```
spring.datasource.username=
spring.datasource.password=
```


### Endpoints

* POST   /stocks/fetch

fromDate should be before toDate

*Example*
###### Request Body
``` json
{
  "companySymbol": "AAPL",
  "fromDate": "2025-02-09",
  "toDate": "2025-02-10"
}
```
###### Response
``` json
Returns Http 200
```

* GET   /stocks/{symbol}?date=YYYY-MM-dd

*Example*

######  Request
``` 
GET   /stocks/AAPL?date=2025-02-10
```
###### Response
``` json
{
    "volume": 33096475,
    "openPrice": 229.57,
    "closePrice": 227.65,
    "highPrice": 230.59,
    "lowPrice": 227.20,
    "date": "2025-02-10",
    "companySymbol": "AAPL"
}
```





