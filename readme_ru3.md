# TASK 2

## Краткое описание
В проект добавлены aspectj, servlets.

Теперь приложение использует клиент - серверную архитектуру для отправки запросов Monitoring - Service.

Audit логгирует все методы Servlets и Services.

Endpoints:

User:

POST http://lovalhost::8080/users/register

     {
         "name": "name",
         "password": "password"
      }
      
GET  http://lovalhost::8080/users/login? name={name}&password={password}

GET  http://lovalhost::8080/users/{id}

GET  http://lovalhost::8080/users

Meter readings:

Только для авторизированных пользователей, для отправки запросов нужеа авторизация

POST: http://lovalhost::8080/meter

      {    
          "number_meter": "number_meter",
          "datalist": [
               {
                  "type": "type",
                   "value": 0.00
                }
            ]
      }
      
GET   http://lovalhost::8080/meter?action=current

GET   http://lovalhost::8080/meter?action=month&month{number}

GET   http://lovalhost::8080/meter?action=hystory

GET   http://lovalhost::8080/meter?action=all

Проект покрыт тестами на 76 ппоцентов
