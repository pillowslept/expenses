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

### Categories

```
{host}/api/categories/ *GET*
```

```
{host}/api/categories/{id} *GET*
```

```
{host}/api/categories/ *POST*
```

```
{host}/api/categories/{id} *PUT*
```

```
{host}/api/categories/{id}/activate *PUT*
```

```
{host}/api/categories/{id}/inactivate *PUT*
```

### Types

```
{host}/api/types/ *GET*
```

```
{host}/api/types/ *POST*
```

```
{host}/api/types/{id}/activate *PUT*
```

```
{host}/api/types/{id}/inactivate *PUT*
```

### Movements

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
{host}/api/movements/ *POST*
```

### PDF reports

```
{host}/api/pdf/reports/generate *GET*
```

```
{host}/api/pdf/reports/month/{month} *GET*
```

```
{host}/api/pdf/reports/year/{year} *GET*
```

```
{host}/api/pdf/reports/month/{month}/year/{year} *GET*
```

### XLS reports

```
{host}/api/excel/reports/month/{month}/year/{year} *GET*
```

> **month** from **1** to **12**
>
> **year** from **0000** to **9999**

## Author

* **Juan Camilo Velásquez Vanegas** - [Juan Camilo Velásquez](https://github.com/pillowslept)