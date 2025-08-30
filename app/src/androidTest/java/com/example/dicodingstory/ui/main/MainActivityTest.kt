package com.example.dicodingstory.ui.main

import androidx.test.espresso.Espresso.*
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.example.dicodingstory.R
import com.example.dicodingstory.ui.login.LoginActivity
import com.example.dicodingstory.utils.EspressoIdlingResource
import org.hamcrest.Matchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {

    @get:Rule
    val activity = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        Intents.init()
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        Intents.release()
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun login_logout_Success() {
        onView(withId(R.id.button_login)).check(matches(isDisplayed())).perform(click())

        intended(hasComponent(LoginActivity::class.java.name))
        onView(withId(R.id.ed_login_email)).check(matches(isDisplayed())).perform(typeText("zaky@mail.com"))
        closeSoftKeyboard()
        onView(withId(R.id.ed_login_password)).check(matches(isDisplayed())).perform(typeText("zakystory"))
        closeSoftKeyboard()
        onView(withId(R.id.button_login)).check(matches(isDisplayed())).perform(click())

        onView(withId(R.id.action_logout)).check(matches(isDisplayed())).perform(click())
        onView(allOf(withId(android.R.id.button1), withText("Logout"))).perform(click())

        onView(withId(R.id.tv_welcome_title)).check(matches(isDisplayed()))
    }
}