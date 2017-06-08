package com.ekocbiyik.tdmdemo.sampleData;

import com.ekocbiyik.tdmdemo.enums.EUserRole;
import com.ekocbiyik.tdmdemo.model.User;
import com.ekocbiyik.tdmdemo.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Created by enbiya on 05.01.2017.
 */
public class Sample {

    @Autowired
    private static IUserService userService;

    public static void executeDatabase() {

//        createSampleUser();
    }

    private static void createSampleUser() {

        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword("admin");
        admin.setRole(EUserRole.ADMIN);
        admin.setFirstName("Enbiya");
        admin.setLastName("Koçbıyık");
        admin.setEmail("enbiya.kocbiyik@ekocbiyik.com");
        admin.setPhone("1234567890");
        admin.setCreationDate(new Date());
        admin.setCreatedBy("System");
        admin.setActive(true);

        userService.save(admin);

        for (int i = 0; i < 10; i++) {

            User testEng = new User();
            testEng.setUsername("testEng" + i);
            testEng.setPassword("testEng" + i);
            testEng.setRole(EUserRole.TEST_ENG);
            testEng.setFirstName("testEng" + i);
            testEng.setLastName("testEng" + i);
            testEng.setEmail("testEng" + i + "@mail.com");
            testEng.setPhone("1234567890");
            testEng.setCreationDate(new Date());
            testEng.setCreatedBy("System");
            testEng.setActive(i % 2 == 0);

            userService.save(testEng);
        }
    }

}
