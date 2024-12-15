package com.josepedevs;

import com.josepedevs.application.usecase.authenticationdata.GetAllAuthenticationDataUseCaseImplTest;
import com.josepedevs.infra.input.rest.authenticationdata.*;
import com.josepedevs.infra.output.persistence.jpa.postgresql.JpaAuthenticationDataPostgreSqlAdapterTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("VerticalTestAuditAUTH")
@SelectClasses({
        //Infra.input.rest
        RestGetAllRegisteredControllerTest.class,
        RestInvalidateTokenControllerTest.class,
        RestLoginControllerTest.class,
        RestRegisterControllerTest.class,
        RestUpdatePasswordControllerTest.class,
        RestUpdateRoleControllerTest.class ,
        //Application
        GetAllAuthenticationDataUseCaseImplTest.class,
        //Infra.output.postgresql
        JpaAuthenticationDataPostgreSqlAdapterTest.class,
        AuthManagerAppTest.class
})
public class VerticalTestAuditAuth {

}

