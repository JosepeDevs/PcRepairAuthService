package com.josepedevs.application.usecase.authenticationdata;

/*
@Service
@RequiredArgsConstructor
public class PatchPasswordTest {

	private final AuthRepository repository;
	private final JwtTokenDataExtractorService jwtReaderService;
	private final PasswordEncoder passwordEncoder;
	private final Logger logger = LoggerFactory.getLogger(PatchPassword.class);

	public boolean patchPassword(String jwtToken, String newpassword) {
		//here is being throw the error
		String username = jwtReaderService.extractUsername(jwtToken);
		Optional<AuthenticationData> userDataAuth = repository.findByUsername(username); 
		AuthenticationData existingUser = userDataAuth.orElseThrow( () -> {
			logger.error("User was not found with username: "+username);
			throw new UserNotFoundException("ha intentado cambiuar la contraseña de un usuario que "
				+ "no existe o el token con las credenciales no lo contenia.", "username");
			} 
		);	
		String hashedPassword = passwordEncoder.encode(newpassword);
		boolean isUserSavedWithChanges = repository.patchPassword(existingUser, hashedPassword);
		if( isUserSavedWithChanges ) {
			logger.info("User updated password correctly.");
			return true;
		} else { 
			logger.info("User could not update password or no changes were detected");
			return false;
		}
	}
	

}
*/