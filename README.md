# Redmine work log for Revolver Ids

## Reason

Sum up your redmine work log on a custom string field named "Revolver number".
To not to spend time using excel for each day, this tool comes into play. 

## What does it do?

Asking once for API secret key of your redmine account, it reads your redmine work logs sorts it into daily chunks, searches for the custom field of 
"Revolver number", sums it up on this and pours out a list like this: 

```
+ ----------------------------------- + ----- +
| Date: 1970-04-01                    |       |
| Revolver Identifier                 | Hours |
+ ----------------------------------- + ----- +
| foo-bar-F                           | 4.50  |
| foo-baz-F                           | 0.25  |
| not provided for #12345             | 0.50  |
| foo-boo-F                           | 0.25  |
| foo-bom-F                           | 2.50  |
+ ----------------------------------- + ----- +
| Total hours                         | 8.00  |
+ ----------------------------------- + ----- +
```

### You could choose various options (4.0.0): 

```
Enter a command: 
(1) for today
(2) for last month
(3) for this actual month
(4) for last week
(5) for this actual week
(6) for yesterday
(0) for ending application.
[1 today]: 
```

### Getting result:

```
... starting redmine API for today ...
[##############################################################] 100%

+ ----------------------------------- + ----- +
| Date: 1970-04-01                    |       |
| Revolver Identifier                 | Hours |
+ ----------------------------------- + ----- +
| foo-bar-F                           | 4.50  |
| foo-baz-F                           | 0.25  |
| not provided for #12345             | 0.50  |
| foo-boo-F                           | 0.25  |
| foo-bom-F                           | 2.50  |
+ ----------------------------------- + ----- +
| Total hours                         | 8.00  |
+ ----------------------------------- + ----- +

````

Redmine Revolver ID will by `foo-bar-F`.

If a ticket does not have a Redmine Revolver ID,
it will be shown as not `provided for #12345`. Ask your boss for a Revolver ID on this ticket or one of it's parents.

## Starting

You could start by compiling your jar file, or use latest compilation in root folder by using version number

`java -jar redmine_worklog_revolver{version}.jar`

Example:

`java -jar redmine_worklog_revolver1.0.0.jar`

Please forgive me, that I included jar file into repository. I did this to enable users to start application without compiling it. 

## Configuration

After first start, you will be asked for your redmine Api Key, could be found in your redmine user account settings. 
At then .configuration/config.properties will be set: 

```
config.default_uri = https://frs.plan.io
config.default_limit = 100
config.date_format = yyyy-MM-dd
config.api_access_key = foobarbaz12345

```
### config.default_uri

String, URI of your redmine API end point. Due to team usage, this is set to a certain customer of plan.io.

### config.default_limit

int, limit of pagination for API call. In case of difficulties in performance set this to smaller chunks. 

### config.date_format

String, date format like yyyy-MM-dd for 1970-04-01 or dd.MM.yyyy for 01.04.1970

### config.api_access_key

String, your secret API key, identifying you as user of redmine. 

### Note: 

You can change values in this file by editing and saving. In case of malformed entries, application could end on exceptions. 

## Issues

* code coverage is not finished
* make use of multi threading on requests
