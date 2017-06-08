package com.ekocbiyik.tdmdemo.utils;

import com.ekocbiyik.tdmdemo.enums.EUserRole;
import com.ekocbiyik.tdmdemo.model.Company;
import com.ekocbiyik.tdmdemo.model.InfrastructureType;
import com.ekocbiyik.tdmdemo.model.User;
import com.ekocbiyik.tdmdemo.service.ICompanyService;
import com.ekocbiyik.tdmdemo.service.IInfrastructureTypeService;
import com.ekocbiyik.tdmdemo.service.IUserService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by enbiya on 06.01.2017.
 */
public class DBDefaultValues {

    public static void initialize() {

        addDefaultCompany();
        initInfrastructureType();
        addDefaultUsers();
    }

    private static void addDefaultCompany() {

        ICompanyService companyService = UtilsForSpring.getSingleBeanOfType(ICompanyService.class);
        if (companyService.getCompanies().size() == 0) {

            Company company = new Company();
            company.setCompanyName("ekocbiyik");
            companyService.save(company);
        }
    }

    private static void addDefaultUsers() {

        IUserService userService = UtilsForSpring.getSingleBeanOfType(IUserService.class);
        ICompanyService companyService = UtilsForSpring.getSingleBeanOfType(ICompanyService.class);

        /**db de kullanıcı yoksa */
        if (userService.getDefaultSysAdminUser().size() < 1) {

            User sysAdmin = new User();
            sysAdmin.setUsername("sysadmin");
            sysAdmin.setPassword(EncryptionUtils.hexMd5("sysadmin"));
            sysAdmin.setRole(EUserRole.SYSADMIN);
            sysAdmin.setFirstName("sysadmin");
            sysAdmin.setLastName("sysadmin");
            sysAdmin.setEmail("sysadmin@mail.com");
            sysAdmin.setPhone("1234567890");
            sysAdmin.setCreationDate(new Date());
            sysAdmin.setCreatedBy("System");
            sysAdmin.setActive(true);
            userService.save(sysAdmin);
        }

        if (userService.getDefaultAdminUser().size() < 1) {

            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(EncryptionUtils.hexMd5("admin"));
            admin.setRole(EUserRole.ADMIN);
            admin.setCompany(companyService.getCompanies().get(0));
            admin.setFirstName("Admin");
            admin.setLastName("Admin");
            admin.setEmail("admin@mail.com");
            admin.setPhone("1234567890");
            admin.setCreationDate(new Date());
            admin.setCreatedBy("System");
            admin.setActive(true);
            userService.save(admin);
        }

        if (userService.getDefaultTestUser().size() < 1) {

            User test = new User();
            test.setUsername("test");
            test.setPassword(EncryptionUtils.hexMd5("test"));
            test.setRole(EUserRole.TEST_ENG);
            test.setCompany(companyService.getCompanies().get(0));
            test.setFirstName("Test");
            test.setLastName("Test");
            test.setEmail("test@mail.com");
            test.setPhone("1234567890");
            test.setCreationDate(new Date());
            test.setCreatedBy("System");
            test.setActive(true);
            userService.save(test);
        }

    }

    private static void initInfrastructureType() {

        IInfrastructureTypeService infTypeService = UtilsForSpring.getSingleBeanOfType(IInfrastructureTypeService.class);

        if (infTypeService.getAllInfrastructureTypes().size() < 1) {

            /** new InfrastructureType("type", "value", "info") */
            List<InfrastructureType> inf = new ArrayList<>();

            // internet tipi değerleri
            inf.add(new InfrastructureType("INTERNET", "NVDSL", " "));
            inf.add(new InfrastructureType("INTERNET", "VDSL", " "));

            // televizyon tipi değerleri
            inf.add(new InfrastructureType("TV", "IPTV", "sonra girilecek"));

            // telefon tipi değerleri
            inf.add(new InfrastructureType("TELEFON", "TOPTAN", "sonra girilecek"));
            inf.add(new InfrastructureType("TELEFON", "PERAKENDE", "sonra girilecek"));

            for (InfrastructureType type : inf) {
                infTypeService.save(type);
            }
        }

    }

}
