#   API仕様

##  イベント作成

### メソッド
-   POST

### URLパラメータ
-   /create

### リクエストパラメータ
```json
{
    "planner":  "planner name",
    "eventName": "eventName(string)",
    "candidateDate": ["date1(string)", "date2", ...],
    "deadline": "deadline(string)",
    "comment":  "comment(string)",
}
```
### レスポンス
```
HTTP/1.1 200 OK
Content-Type: application/json;charset=utf-8
```

### レスポンスボディ
```json
{
    "id":   1,
    "name": "eventName",
    "candidateDate": ["date1", "date2"],
    "deadline": "deadline",
    "comment":  "comment"
}
```

##  イベント選択
### Method
-   POST
### URL
-   /view
### Request Parameter
```json
    {"id":  1}
```
### Response
```
HTTP/1.1 200 OK
Content-Type:application/json;charset=utf-8
```
### Response Body
```json
{
    "id":   1,
    "name": "eventName",
    "candidateDate": ["date1", "date2"],
    "deadline": "deadline",
    "comment":  "comment"
}
```

##  イベント更新
### Method
-   POST
### URL
-   /event/update?id=event-id
### Request Parameter
```json
{
    "id": 1,
    "name": "newEventName",
    "candidateDate": null,
    "deadline": null,
    "comment":  null,
}
```
### Response
```
HTTP/1.1 200 OK
Content-Type:application/json;charset=utf-8
```

### Response Body
```json
{
    "id":   1,
    "name": "eventName",
    "candidateDate": ["date1", "date2"],
    "deadline": "deadline",
    "comment":  "comment"
}
```

##イベント削除
### Method
-   POST

### URL
-   /event/delete?id=event-id

### Request Parameter
```json
{"id":  1}
```

###Response
```
HTTP/1.1 200 OK
```

##  投票
### Method
-   POST
### URL
-   /event/vote
### Request Parameter
```json
{
    "participantName":  "participant name",
    "id":   1,
    "vote": [
        {"date": "voting status"},
        {"date": "voting status"}
    ]
}
```
### Response
```
HTTP/1.1 200 OK
Content-Type:application/json;charset=utf-8
```
### Response Body
```json
{
    "participantName":  "participant name",
    "id":   1,
    "vote": [
        {"date": "voting status"},
        {"date": "voting status"}
    ]
}
```
##  投票結果表示
### Method
-   GET
### URL
-   /event/view?id=event-id
### Request
null
### Response
```
HTTP/1.1 200 OK
Content-Type:application/json;charset=utf-8
```
### Response Body
```json
{
    "id":  1,
    "eventName": "event name",
    "votingResults": [
        {
            "date1": [
                {"maru": 1},
                {"batu": 0},
                {"sankaku":2}
        ]},
        {
            "date2": [
                {"maru": 0},
                {"batu": 1},
                {"sankaku":1}
            ]
        }
    ]

}
```
#   DB設計
##  Database name:  eventapp
##  Table name: event
作成されたイベントのテーブル
|column name|type|key|
|:---:|:---:|:---:|
|id|int|primary key|
|event-name|varchar(32)
|deadline|timestamp
|comment|varchar(256)
|planner|varchar(32)
|status|bool

##  Table name: vote
投票結果を格納するテーブル
|column name|type|key|
|:---:|:---:|:---:|
|event id|id|key|
|participant|varchar(32)|key|
|voting date|date or timestamp|key|
|voting status|int|