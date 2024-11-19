import com.josepdevs.Application.PatchUserRoleUseCaseImplTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

import com.josepdevs.Application.GetAllusersUseCaseImplTest;
import com.josepdevs.Application.InvalidateUserTokenUseCaseImplTest;
import com.josepdevs.Infra.input.rest.GetAllRegisteredControllerTest;
import com.josepdevs.Infra.input.rest.InvalidateUserTokenUseCaseImplControllerTest;
import com.josepdevs.Infra.input.rest.LoginUserUseCaseImplControllerTest;
import com.josepdevs.Infra.input.rest.RegisterUserUseCaseImplControllerTest;
import com.josepdevs.Infra.input.rest.UpdatePasswordControllerTest;
import com.josepdevs.Infra.input.rest.UpdateRoleControllerTest;
import com.josepdevs.Infra.output.postgresql.JpaAuthenticationDataPostgreSqlAdapterTest;

@Suite
@SuiteDisplayName("VerticalTestAuditAUTH")
@SelectClasses({ 
		//Infra.input.rest
		GetAllRegisteredControllerTest.class, 
		InvalidateUserTokenUseCaseImplControllerTest.class,
		LoginUserUseCaseImplControllerTest.class,
		RegisterUserUseCaseImplControllerTest.class,
		UpdatePasswordControllerTest.class,
		UpdateRoleControllerTest.class ,
		//Application
		GetAllusersUseCaseImplTest.class,
		InvalidateUserTokenUseCaseImplTest.class,
		PatchUserRoleUseCaseImplTest.class,
		//Infra.output.postgresql
		JpaAuthenticationDataPostgreSqlAdapterTest.class
})
public class VerticalTestAuditAUTH {

}

