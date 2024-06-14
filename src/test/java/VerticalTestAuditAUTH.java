import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

import com.josepdevs.Application.GetAllRegisteredUseCaseTest;
import com.josepdevs.Application.InvalidateTokenTest;
import com.josepdevs.Infra.input.rest.GetAllRegisteredControllerTest;
import com.josepdevs.Infra.input.rest.InvalidateTokenControllerTest;
import com.josepdevs.Infra.input.rest.LoginControllerTest;
import com.josepdevs.Infra.input.rest.RegisterControllerTest;
import com.josepdevs.Infra.input.rest.UpdatePasswordControllerTest;
import com.josepdevs.Infra.input.rest.UpdateRoleControllerTest;
import com.josepdevs.Infra.output.postgresql.UserPostgreSqlAdapterTest;

@Suite
@SuiteDisplayName("VerticalTestAuditAUTH")
@SelectClasses({ 
		//Infra.input.rest
		GetAllRegisteredControllerTest.class, 
		InvalidateTokenControllerTest.class,
		LoginControllerTest.class,
		RegisterControllerTest.class,
		UpdatePasswordControllerTest.class,
		UpdateRoleControllerTest.class , 
		//Application
		GetAllRegisteredUseCaseTest.class,
		InvalidateTokenTest.class,
		//Infra.output.postgresql
		UserPostgreSqlAdapterTest.class
})
public class VerticalTestAuditAUTH {

}

