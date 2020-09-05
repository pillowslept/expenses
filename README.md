# Expenses

API to manage expenses and incomes

## What can I do?

* Create and manage your own categories

* Save a registry for every money movement

* Generate **`PDF`** and **`XLS`** reports with all the movements, filtering by some fields such as month and year

* Visualize charts with the summary of the movements

## API services

The **`application.yml`** contains some basic configurations, such as port, context path, database and more.

Port **`9000`** and context path **`Expenses`** are in use

> localhost:9000/Expenses

### Categories

```
/api/categories/ *GET*
```

```
/api/categories/{id} *GET*
```

```
/api/categories/ *POST*
```

```
/api/categories/{id} *PUT*
```

```
/api/categories/{id}/activate *PUT*
```

```
/api/categories/{id}/inactivate *PUT*
```

### Types

```
/api/types/ *GET*
```

```
/api/types/ *POST*
```

```
/api/types/{id}/activate *PUT*
```

```
/api/types/{id}/inactivate *PUT*
```

### Movements

```
/api/movements/{id} *GET*
```

```
/api/movements/filters (pageNumber, pageSize, value, year, month and sortType optional as query) *GET*
```

```
/api/movements/ *POST*
```

```
/api/movements/{id} *PUT*
```

### PDF reports

```
/api/pdf/reports/generate *GET*
```

```
/api/pdf/reports/month/{month} *GET*
```

```
/api/pdf/reports/year/{year} *GET*
```

```
/api/pdf/reports/month/{month}/year/{year} *GET*
```

### XLS reports

```
/api/excel/reports/month/{month}/year/{year} *GET*
```

> **month** from **1** to **12**
>
> **year** from **0000** to **9999**

## Author

* **Juan Camilo Velásquez Vanegas** - [Juan Camilo Velásquez](https://github.com/pillowslept)