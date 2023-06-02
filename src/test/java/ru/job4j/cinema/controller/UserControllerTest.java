package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserControllerTest {
    private HttpServletRequest request;

    private UserService userService;

    private UserController userController;

    @BeforeEach
    public void initServices() {
        request = new MockHttpServletRequest();
        userService = mock(UserService.class);
        userController = new UserController(userService);
    }

    @Test
    public void whenGetRegistrationPage() {
        String view = userController.getRegistrationPage();

        assertThat(view).isEqualTo("users/register");
    }

    @Test
    public void whenRegisterUserThenRedirectFilmsPage() {
        User user = new User("fullName", "email", "pass");
        when(userService.save(any())).thenReturn(Optional.of(user));

        Model model = new ConcurrentModel();
        String view = userController.registerUser(user, model);

        assertThat(view).isEqualTo("redirect:/films");
    }

    @Test
    public void whenRegisterUserThenGetPageWithErrorMessage() {
        User user = new User("fullName", "email", "pass");
        Exception expectedException = new RuntimeException("Пользователь с такой почтой уже существует");
        when(userService.save(any())).thenReturn(Optional.empty());

        Model model = new ConcurrentModel();
        String view = userController.registerUser(user, model);
        Object actualExceptionMessage = model.getAttribute("error");

        assertThat(view).isEqualTo("users/register");
        assertThat(actualExceptionMessage).isEqualTo(expectedException.getMessage());
    }

    @Test
    public void whenGetLoginPage() {
        String view = userController.getLoginPage();

        assertThat(view).isEqualTo("users/login");
    }

    @Test
    public void whenLoginUserThenRedirectFilmsPage() {
        User user = new User("fullName", "email", "pass");
        when(userService.findByEmailAndPassword(any(), any())).thenReturn(Optional.of(user));

        Model model = new ConcurrentModel();
        String view = userController.loginUser(user, model, request);

        assertThat(view).isEqualTo("redirect:/films");
    }

    @Test
    public void whenLogoutUserThenRedirectFilmsPage() {
        String view = userController.logout(request.getSession());

        assertThat(view).isEqualTo("redirect:/films");
    }
}