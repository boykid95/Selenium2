package vn.agest.selenium.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import vn.agest.selenium.enums.Condition;
import vn.agest.selenium.utils.WaitHelper;

public final class ConditionExecutor {

    private ConditionExecutor() {
    }

    public static WebElement handle(By locator, Condition condition) {

        return switch (condition) {
            case VISIBLE -> WaitHelper.waitForVisible(locator);
            case CLICKABLE -> WaitHelper.waitForClickable(locator);
            case PRESENT -> WaitHelper.waitForPresence(locator);

            case INVISIBLE -> {
                WaitHelper.waitForInvisible(locator);
                yield null;
            }
        };
    }
}

