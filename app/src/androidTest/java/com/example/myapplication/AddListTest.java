package com.example.myapplication;

import android.support.test.rule.ActivityTestRule;

import androidx.test.espresso.Espresso;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class AddListTest {

    @Rule
    public ActivityTestRule<AddList> mActivityTestRule = new ActivityTestRule<AddList>(AddList.class);

    private String listName = "TestList";
    private String itemName = "TestItem";

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testUserListNameInputScenario() {
        //perform button click (New  List)
        Espresso.onView(withId(R.id.button3)).perform(click());
        //input text in the edit text
        Espresso.onView(withId(R.id.listName)).perform(typeText(listName));
        //perform button click
        Espresso.onView(withId(R.id.button)).perform(click());
        //close soft keyboard
        Espresso.closeSoftKeyboard();
        //go back
        Espresso.pressBack();
        //checking text in text view
        Espresso.onView(withId(R.id.shoppingLists)).check(matches(withText(listName)));
    }

    @Test
    public void testAddItemScenario(){
        //perform button click (New  List)
        Espresso.onView(withId(R.id.button3)).perform(click());
        //input text in the edit text (List Name)
        Espresso.onView(withId(R.id.listName)).perform(typeText(listName));
        //perform button click (Save)
        Espresso.onView(withId(R.id.button)).perform(click());
        //input text in the edit text (List Item)
        Espresso.onView(withId(R.id.item)).perform(typeText(itemName));
        //perform button click (Add Item)
        Espresso.onView(withId(R.id.button5)).perform(click());
        //close soft keyboard
        Espresso.closeSoftKeyboard();
        //perform button click (Show List)
        Espresso.onView(withId(R.id.button4)).perform(click());
        //checking text in text view
        Espresso.onView(withId(R.id.scrollView2)).check(matches(withText(itemName)));
    }

    @After
    public void tearDown() throws Exception {
    }

}