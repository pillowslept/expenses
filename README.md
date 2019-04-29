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

```
{host}/api/categories/ *PUT*
```

```
{host}/api/types/ *PUT*
```

```
{host}/api/movements/ *PUT*
```

```
{host}/api/movements/{id} *GET*
```

```
{host}/api/movements (pageNumber and pageSize optional as query) *GET*
```

```
{host}/api/movements/month/{month}/year/{year} (pageNumber and pageSize optional as query) *GET*
```

```
{host}/api/pdf/reports/generate *GET*
```

```
{host}/api/pdf/reports/month/{month} *GET*
```

```
{host}/api/pdf/reports/month/{month}/year/{year} *GET*
```

```
{host}/api/pdf/reports/year/{year} *GET*
```

```
{host}/api/excel/reports/month/{month}/year/{year} *GET*
```

> **month** from **1** to **12**
>
> **year** from **0000** to **9999**

## Author

* **Juan Camilo Velásquez Vanegas** - [Juan Camilo Velásquez](https://github.com/pillowslept)