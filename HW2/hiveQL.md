# Работа с Hive
 1 Сделать таблицу artists в Hive и вставить туда значения, используя датасет 
https://www.kaggle.com/pieca111/music-artists-popularity - 15 баллов

Сделано, на каждом из скриншотов приводятся результаты запроса
```
SELECT COUNT(*) FROM artists;
```

 2 Используя Hive найти (команды и результаты записать в файл и добавить в 
репозиторий):

 a) Исполнителя с максимальным числом скробблов - 5 баллов
 ```
SELECT artist_mb
FROM artists
WHERE scrobbles_lastfm IN (SELECT MAX(scrobbles_lastfm) FROM artists);
```
Ответ: `The Beatles`

b) Самый популярный тэг на ластфм - 10 баллов

```
SELECT
    tag,
    COUNT(tag) AS freq
FROM (
SELECT 
    tag
FROM artists
LATERAL VIEW explode(split(tags_lastfm, '; ')) tags_lastfm AS tag
WHERE tag <> '') AS tags
GROUP BY tag
ORDER BY freq DESC
LIMIT 1;
```

Ответ: `seen live`

c) Самые популярные исполнители 10 самых популярных тегов ластфм - 10 
баллов

Сначала создадим таблицу с 10 самыми популярными тегами:
```
CREATE TEMPORARY TABLE popular_tags AS
SELECT
    tag,
    COUNT(tag) AS freq
FROM (
SELECT 
    tag
FROM artists
LATERAL VIEW explode(split(tags_lastfm, '; ')) tags_lastfm AS tag
WHERE tag <> '') AS tags
GROUP BY tag
ORDER BY freq DESC
LIMIT 10;
```
Затем воспользуемся ей, и оставим лишь артистов, для которых упоминаются эти теги:
```
SELECT DISTINCT
    t.artist_lastfm AS artist,
    t.listeners_lastfm AS count_listeners
FROM (
SELECT
    artist_lastfm,
    listeners_lastfm,
    tag
FROM artists
LATERAL VIEW explode(split(tags_lastfm, '; ')) tags_lastfm AS tag) AS t
INNER JOIN popular_tags ON t.tag = popular_tags.tag
ORDER BY t.listeners_lastfm DESC
LIMIT 10;
```
![q3](https://user-images.githubusercontent.com/59476789/137592458-d3a23603-33cc-420f-9c64-8b82167f35d2.PNG)

d) Любой другой инсайт на ваше усмотрение - 10 баллов

Найдем 5 самых популярных российских рок исполнителей:
```
SELECT DISTINCT
    artist_lastfm,
    listeners_lastfm
FROM artists
LATERAL VIEW explode(split(tags_lastfm, '; ')) tags_lastfm AS tag
WHERE country_lastfm = 'Russia' AND tag = 'rock'
ORDER BY listeners_lastfm DESC
LIMIT 5;
```
![q4](https://user-images.githubusercontent.com/59476789/137592463-25e882f5-d645-4e6c-96f2-fcbab3f64b5e.PNG)

