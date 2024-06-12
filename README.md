# News API

REST application developed using Java and Spring Boot for fetching news articles. It has been integrated with a public third party API provided by news-api.org (https://newsapi.org).
Caching has also been implemented to improve performance.
 

## API Reference

#### Get articles by title

Fetch articles with a specific title

```http
  GET /getByTitle/{title}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `title` | `string` | **Required**. Title of the article |

#### Get n latest articles by keyword

Fetch n latest articles containing a specific keyword in title, description or keyword

```http
  GET /getNLatestByKeyword
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `n`      | `Integer` | **Required**. number of articles (n > 0, n <= 100) |
| `keyword`| `String`  | **Required**. keyword |

#### Get articles by author and keyword

Fetch articles written by a specific author and containing a specific keyword in title, description or keyword

```http
  GET /searchByAuthorAndKeyword
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `author`      | `String` | **Required**. author |
| `keyword`| `String`  | **Required**. keyword |

#### Get articles by keyword in title

Fetch articles by a specific keyword appearing in title

```http
  GET /searchByKeywordInTitle
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `keyword`| `String`  | **Required**. keyword |

#### Get headlines by country

Fetch top headlines for a specific country

```http
  GET /getHeadlinesByCountry
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `country`| `String`  | **Required**. country code |

Country codes permitted: ae, ar, at, au, be, bg, br, ca, ch,
            cn, co, cu, cz, de, eg, fr, gb, gr, hk, hu, id, ie, il, in, it, jp, kr, lt, lv, ma, mx, my, ng, nl, no, nz, ph, pl, pt, ro, rs, ru, sa, se, sg,
            si, sk, th, tr, tw, ua, us, ve, za

#### Get headlines by keyword

Fetch top headlines by keyword. Optionally limit the number of headlines

```http
  GET /getTopHeadlinesByKeyword
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `keyword`| `String`  | **Required**. keyword |
| `n`| `Integer`  | **Optional**. n (n > 0, n <= 100) |



## How to run

Follow the steps listed below to run and test this project:

- Run the main java class from NewsApiApplication.java
- Execute the following example urls in your browser. Alternatively, import the postman collection found at https://drive.google.com/file/d/10fVL09lyx-BeCGdTg85pESjmCSHcgs2J/view?usp=share_link into your postman app (recommended). It contains the Http requests listed below. If you execute these requests through postman, you should also see each request taking significantly less time on second or any subsequent execution since caching has been implemented.

  http://localhost:8080/service/getByTitle/Learning%20to%20Live%20With%20Google's%20AI%20Overviews


  http://localhost:8080/service/searchByKeywordInTitle?keyword=finance


  http://localhost:8080/service/getHeadlinesByCountry?country=us


  http://localhost:8080/service/getHeadlinesByCountry?country=zm


  http://localhost:8080/service/getTopHeadlinesByKeyword?keyword=election&n=10


  http://localhost:8080/service/getTopHeadlinesByKeyword?keyword=election


  http://localhost:8080/service/getNLatestByKeyword?n=8&keyword=cricket


  http://localhost:8080/service/getNLatestByKeyword?n=150&keyword=cricket


  http://localhost:8080/service/searchByAuthorAndKeyword?keyword=finance&author=Rocio%20Fabbro











