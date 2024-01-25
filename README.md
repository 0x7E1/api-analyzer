# API Access Load Analyzer
###### Technical task for Skai.io interview

## Input example:
`[CSV file]`
**! _Ensure that resource file does not contain headers row_ (IPv4, Timestamp ...) !**

| IPv4          | Timestamp                 | HTTP method | Endpoint             | Status |
|---------------|---------------------------|-------------|----------------------|--------|
| 192.168.2.212 | 28/07/2006:10:27:10-0300  | GET         | /user/try            | 200    |
| 192.168.2.212 | 28/07/2006:10:22:04-0300  | GET         | /                    | 200    |
| 192.168.2.220 | 28/07/2006:10:25:04-0300  | PUT         | /save                | 200    |
| 192.168.2.111 | 28/07/2006:10:25:04-0300  | POST        | /save                | 403    |
| 192.168.2.111 | 28/07/2006:10:29:04-0300  | GET         | /details             | 200    |
| 192.168.2.111 | 28/07/2006:10:29:04-0300  | PUT         | /save                | 401    |
| 192.168.2.111 | 28/07/2006:10:29:04-0300  | POST        | /details             | 200    |
| 192.168.2.111 | 28/07/2006:10:29:04-0300  | PUT         | /save                | 200    |
| 192.168.2.212 | 28/07/2006:10:27:10-0300  | GET         | /user/try            | 200    |
| 192.168.2.212 | 28/07/2006:10:22:04-0300  | GET         | /invalid/            | 400    |
| 0.1.1.1       | 28/07/2006:10:22:04-0300  | GET         | /invalid/ip          | 200    |
| 192.168.0.1   | 2011-12-03T10:15:30+01:00 | GET         | /invalid/datetime    | 200    |
| 192.168.0.1   | 28/07/2006:10:22:04-0300  | XYZ         | /invalid/http-method | 200    |
| 192.168.0.1   | 28/07/2006:10:22:04-0300  | GET         | invalid/url.         | 200    |
| 192.168.0.1   | 28/07/2006:10:22:04-0300  | GET         | /invalid/status-code | 0      |

## Output example:
`[Generic mode]`

| Endpoint  | HTTP method | Calls |
|-----------|-------------|-------|
| /save     | PUT         | 3     |
| /user/try | GET         | 2     |
| /details  | GET         | 1     |

`[Embedded mode]` 
```
Results:
/save – PUT – 3 times
/user/try – GET – 2 times
/details – GET – 1 time

---- Statistics ----
Requests per seconds:
2006-07-28T10:29:04 – 4 requests
2006-07-28T10:22:04 – 1 request
2006-07-28T10:25:04 – 2 requests
2006-07-28T10:27:10 – 2 requests
--------------------

----- Counters -----
Total rows – 15
Valid rows – 9
Processed total time – 0.011 seconds
--------------------
```
In generic **web-server** mode `[analyzer.embedded-mode.enabled = false]` the console output will be almost the same,
but without `Results` block.

The `Results` block will be returned in CSV format as a file for downloading.

### How to add new statistic parameters:
1. Add new column into **_statistic.csv_** file.
2. Extend `ApiLog` and `ApiLogDto` _model_ classes with new desired field.
3. Extend `CsvDataLoader.fromStream()` default method for binding added parameter with new _model_ fields.
4. Implement new validator inherited from `ApiLogValidator` service.
5. Implement desired logic for your statistic's parameter.