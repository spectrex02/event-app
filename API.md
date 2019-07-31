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

##  イベント取得（全件）フィルター条件入れれるやつ
### Method
-   GET
### URL
-   /events?dateFron=xxx&dateEnd=xxxx

##  イベント取得（１件）
### Method
-   GET
### URL
-   /event/${eventId}
### Request Parameter
- eventId: number(require)
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
-   /event/${eventId}
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
-   DELETE

### URL
-   /event/${eventId}

### Request Parameter
```json
{"id":  1}
```

### Response
```
HTTP/1.1 200 OK
```

##  投票
### Method
-   POST
### URL
-   /event/$eventId/vote
### Request Parameter
```json
{
    "participantName":  "participant name",
    "vote": [
        {"date1": 1},
        {"date2": 0}
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
    "vote": [
        {"date1": 1},
        {"date2": 0}
    ]
}
```
##  投票結果表示
### Method
-   GET
### URL
-   /event/$e{ventId}/results
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
|event_name|varchar(32)
|deadline|timestamp
|comment|varchar(256)
|planner|varchar(32)
|status|bool

##  Table name: vote
投票結果を格納するテーブル
|column name|type|key|
|:---:|:---:|:---:|
|event_id|id|key|
|participant_name|varchar(32)|key|
|voting_date|timestamp|key|
|voting_status|int|

##  Table name: candidate_date
イベントの候補日
|column name|type|key|
|:---:|:---:|:---:|
|event_id|int|key|
|date|timestamp|