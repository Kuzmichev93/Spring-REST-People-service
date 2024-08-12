<div> Создан REST сервис, который содержит контроллеры: PeopleController,RegistrationController и AuthController.</div>
<div>В PeopleController реализованы методы GET, POST, PUT и DELETE.</div>
<div>В RegistrationController с использованием spring security реализовано: регистрация, аутентификация, авторизация и выход пользователя из системы.</div>
<div>
<div>В AuthController реализовано получение JWT токенов access token и refresh token.</div>
    <li> Метод api/auth/login возвращает access token и refresh token.</li>
    <li> Метод api/auth/accesstoken возвращает новый access token.</li>
    <li> Метод api/auth/refreshtoken возвращает новый refresh token.</li>
</div>
<div>Класс AuthenticationFilter реализует аутентификацию, авторизацию по access token и basic auth.</div>