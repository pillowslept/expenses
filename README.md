# Expenses

API to manage expenses and incomes

## What can I do?

* Create and manage your own categories

* Save a registry for every money movement

* Generate **`PDF`** and **`XLS`** reports with all the movements, filtering by some fields such as month and year

* Visualize charts with the summary of the movements

## API services

The **`application.yml`** file was configured to use **`9000`** port as default.

```
http://localhost:9000/Expenses/api/categories/ *PUT*
```

```
http://localhost:9000/Expenses/api/types/ *PUT*
```

```
http://localhost:9000/Expenses/api/movements/ *PUT*
```

```
http://localhost:9000/Expenses/api/movements/ *GET*
```

```
http://localhost:9000/Expenses/api/movements/{id} *GET*
```

```
http://localhost:9000/Expenses/api/movements/all/{pageNumber}/{pageSize} *GET*
```

```
http://localhost:9000/Expenses/api/movements/byMonthAndYear/{month}/{year}/{pageNumber}/{pageSize} *GET*
```

```
http://localhost:9000/Expenses/api/pdf/reports/generate *GET*
```

```
http://localhost:9000/Expenses/api/pdf/report/byMonth/{month} *GET*
```

```
http://localhost:9000/Expenses/api/pdf/report/byMonthAndYear/{month}/{year} *GET*
```

```
http://localhost:9000/Expenses/api/pdf/report/byYear/{year} *GET*
```

```
http://localhost:9000/Expenses/api/excel/report/byMonthAndYear/{month}/{year} *GET*
```

> **month** from **1** to **12**
>
> **year** from **0000** to **9999**

## Author

* **Juan Camilo Velásquez Vanegas** - [Juan Camilo Velásquez](https://github.com/pillowslept)