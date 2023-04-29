# <span style="color:#59afe1"> **Networkchat**.</span>
## *Многопоточный сетевой чат*
### Модули:

- server - серверная часть чата с возможностью подключения клиентов через порт из файла настроек (Setting.txt).
- client - клиентская часть чата с интрефейсом на базе библиотеки java swing. Настройки для подключения берет из файла Setting.txt. Выход из чата происходит по команде “/exit”.
- connection - содержит класс для связи серверной и клиентской части и интерфейс реализуемый этими модулями. Так же содержит класс для получения настроек соединения из файла Setting.txt.

telnet connection test:
![telnet_connection_test](/src/main/resources/telnet_connection_test.png)

client connection test:
![client_connection_test](src/main/resources/client_connection_test.png)