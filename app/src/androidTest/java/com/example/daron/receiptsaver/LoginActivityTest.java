package com.example.daron.receiptsaver;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.daron.receiptsaver.loginTest.LoginActivity2;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;



@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity2> mActivityRule = new ActivityTestRule<>(LoginActivity2.class);

    @Test
    public void checkIfButtonVisible() {
        onView(withId(R.id.loginButton)).check(matches(isDisplayed()));
    }
}