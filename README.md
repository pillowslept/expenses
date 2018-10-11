# Expenses

Services to manage expenses and incomes

## What can I do?

* Create and manage your own categories

* Save a registry for every money movement

* Generate a PDF report with all the movements

* Visualize a chart with the summary of the movements

## Defined services

```
http://localhost:9000/Expenses/api/category/create *PUT*
```

```
http://localhost:9000/Expenses/api/type/create *PUT*
```

```
http://localhost:9000/Expenses/api/movement/create *PUT*
```

```
http://localhost:9000/Expenses/api/movement/all *GET*
```

```
http://localhost:9000/Expenses/api/report/generate *GET*
```

```
http://localhost:9000/Expenses/api/report/byMonth/{month} *GET*
```

## Author

* **Juan Camilo Velásquez Vanegas** - [Juan Camilo Velásquez](https://github.com/pillowslept)