package com.anshuit.writeit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.anshuit.writeit.constants.GlobalConstants;
import com.anshuit.writeit.entities.Category;
import com.anshuit.writeit.entities.Role;
import com.anshuit.writeit.repositories.CategoryRepository;
import com.anshuit.writeit.services.RoleService;

@SpringBootApplication
public class WriteItBackendApplication implements ApplicationRunner {

	@Autowired
	private RoleService roleService;

	@Autowired
	private CategoryRepository categoryRepository;

	public static void main(String[] args) {
		SpringApplication.run(WriteItBackendApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		// CREATE 2 ROLES IF NOT EXISTS WITH CUSTOM ID
		int roleId1 = GlobalConstants.DEFAULT_ROLE_ONE_ID;
		int roleId2 = GlobalConstants.DEFAULT_ROLE_TWO_ID;

		roleService.getRoleByIdOptional(roleId1)
				.orElseGet(() -> roleService.saveOrUpdateRole(new Role(GlobalConstants.DEFAULT_ROLE_ONE)));

		roleService.getRoleByIdOptional(roleId2)
				.orElseGet(() -> roleService.saveOrUpdateRole(new Role(GlobalConstants.DEFAULT_ROLE_TWO)));

		// CREATE 3 CATEGORIES BY DEFAULT
		Category category;
		if (categoryRepository.findById(1).isEmpty()) {
			category = new Category();
			category.setCategoryName("Technology");
			category.setCategoryDescription("Content regarding Tools and Technology.");
			categoryRepository.save(category);
		}
		if (categoryRepository.findById(2).isEmpty()) {
			category = new Category();
			category.setCategoryName("Sports");
			category.setCategoryDescription("Content regarding Sports.");
			categoryRepository.save(category);
		}
		if (categoryRepository.findById(3).isEmpty()) {
			category = new Category();
			category.setCategoryName("Bollywood");
			category.setCategoryDescription("Content regarding Bollywood.");
			categoryRepository.save(category);
		}
	}
}
