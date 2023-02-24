# Jmix Property Condition Empty String Problem

There is a problem when using the Property Conditions in Jmix.

When using `PropertyCondition::equal` and passing in an empty string, it will interpret that in the query,
to simply skip this condition.

When passing in an empty String the resulting SQL statement looks like:

```sql
SELECT ID,
       CREATED_BY,
       CREATED_DATE,
       DELETED_BY,
       DELETED_DATE,
       LAST_MODIFIED_BY,
       LAST_MODIFIED_DATE,
       NAME,
       VERSION
FROM CUSTOMER
WHERE (DELETED_DATE IS NULL)
```

Expected SQL would be:

```sql
SELECT ID,
       CREATED_BY,
       CREATED_DATE,
       DELETED_BY,
       DELETED_DATE,
       LAST_MODIFIED_BY,
       LAST_MODIFIED_DATE,
       NAME,
       VERSION
FROM CUSTOMER
WHERE (DELETED_DATE IS NULL)
AND NAME = ''
```

When using `query()` it works correctly.

The situation is described in full detail in the Integration test: [PropertyConditionEmptyStringProblemTest](src/test/java/com/company/problem/PropertyConditionEmptyStringProblemTest.java).
