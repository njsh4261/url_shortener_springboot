# URL Shortener
[Django로 구현했던 URL Shortener](https://github.com/njsh4261/url_shortener)를 Spring Boot로 재구현한 프로젝트

# 기술 스택
- backend: Spring Boot 2.6.1
- frontend: HTML, Javascript, CSS(Bootstrap), JQuery
- database: MariaDB

# 실행 환경 준비
* OpenJDK 11 및 MariaDB 설치가 필요
* `/src/main/resources/application.properties`에서 DB url, username, password를 개인 환경에 맞춰서 수정
* `/gradlew build`(Mac/Linux) 또는 `./gradlew.bat build`(Windows)로 프로젝트 빌드

# 실행 방법
```
java -jar ./build/libs/UrlShortener-0.0.1-SNAPSHOT.jar
```

# APIs

|API|설명|
|:---|:----|
| `GET /`|단축하고자 하는 URL을 입력 및 단축된 URL을 확인하는 page 제공|
| `POST /url-enc` | URL 단축을 요청하는 API |
| `GET /[shorten_url]` | `[shorten_url]`에 해당하는 원본 URL을 DB에서 검색하여 제공|

- `POST /url-enc`의 경우 Request body에 필수로 포함해야 할 attribute / Response body에 포함된 attribute 존재
    - Request body 예시
        ```
        {
            "url": "https://www.github.com/"
        }
        ```
    - Response body 예시
        ```
        {
            "shorten_url": "http://localhost:8088/EMJD"
            "message": "Success! You may copy the shorten URL above."
        }
        ```
