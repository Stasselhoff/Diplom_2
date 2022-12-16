import generateData.FakeUser;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

@DisplayName("Логин пользователя")
public class UserLoginTest extends BaseClass{


    @Before
    @DisplayName("Создать пользователя со сгенерированными пользовательскими данными")
    public void setUp() throws InterruptedException {
        user = FakeUser.getRandomUserData();
        Response response = userClient.createUser(user);
        accessToken = response.path("accessToken");
        TimeUnit.SECONDS.sleep(2);
    }

    @Test
    @DisplayName("Логин пользователя")
    public void userLoginTest(){
        Response response = userClient.loginUser(user);
        response
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue())
                .body("user", notNullValue());
    }


    @Test
    @DisplayName("Неверный логин пользователя")
    public void userInvalidLoginTest(){
        User user = FakeUser.getRandomUserData();
        user.setName("");
        Response response = userClient.loginUser(user);
        response
                .then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @After
    @DisplayName("Удаление пользователя")
    public void tearDown() {
        if (accessToken != null) userClient.deleteUser(accessToken);
    }
}

