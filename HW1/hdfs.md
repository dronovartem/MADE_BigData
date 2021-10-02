# Работа с HDFS

### Часть 1.

1. Создайте папку в корневой HDFS-папке  
    ```
    hdfs dfs -mkdir /tmp
    ```
2. Создайте в созданной папке новую вложенную папку.  
    ```
    hdfs dfs -mkdir /tmp/tmp 
    ```
3.  Что такое Trash в распределенной FS? Как сделать так, чтобы файлы удалялись сразу, минуя “Trash”? 
    ```
    Trash - помогает избежать случайного удаления файлов. При удалении файл изначально попадает в Trash, который
    который периодически очищается. Чтобы файлы не попадали в Trash, можно воспользоваться флагом -skipTrash.
    ```
4. Создайте пустой файл в подпапке из пункта 2.  
    ```
    hdfs dfs -touchz /tmp/tmp/file.txt 
    ```
5. Удалите созданный файл.  
    ```
    hdfs dfs -rm -skipTrash /tmp/tmp/file.txt 
    ```
6. Удалите созданные папки.  
    ```
    hdfs dfs -rm -r /tmp 
    ```

### Часть 2.

1. Скопируйте любой в новую папку на HDFS
    ```
    hdfs dfs -mkdir /tmp
    hdfs dfs -put data.csv /tmp/data.csv 
    ```
2. Выведите содержимое HDFS-файла на экран.  
    ```
    hdfs dfs -cat /data.csv 
    ```
3. Выведите содержимое нескольких последних строчек HDFS-файла на экран. 
    ```
    hdfs dfs -tail /data.csv 
    ```
4. Выведите содержимое нескольких первых строчек HDFS-файла на экран.  
    ```
    hdfs dfs -head /data.csv
    ```
5. Переместите копию файла в HDFS на новую локацию.  
    ```
    hdfs dfs -mkdir /tmp2
    hdfs dfs -cp /tmp/data.csv /tmp2/data.csv
    ```  


### Часть 3.

1. Изменить replication factor для файла. Как долго занимает время на увеличение / 
уменьшение числа реплик для файла?
    ```
    hdfs dfs -setrep -w 2 /data.csv
    hdfs dfs -setrep -w 3 /data.csv
    Время выполнения обеих операций составило примерно 11 секунд (для датасета из части MapReduce).
    ```
2. Найдите информацию по файлу, блокам и их расположениям с помощью "hdfs fsck".  
    ```
    hdfs fsck /data.csv -files -blocks -locations
    ```
3.  Получите информацию по любому блоку из п.2 с помощью "hdfs fsck -blockId”. 
Обратите внимание на Generation Stamp (GS number). 
    ```
    hdfs fsck -blockId blk_1073741840
    ```