package com.company.problem;

import com.company.problem.entity.Customer;
import io.jmix.core.DataManager;
import io.jmix.core.querycondition.PropertyCondition;
import io.jmix.core.security.SystemAuthenticator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JmixPropertyConditionEmptyStringProblemApplicationTests {

	@Autowired
	DataManager dataManager;
	@Autowired
	SystemAuthenticator systemAuthenticator;

	@BeforeEach
	void setUp() {
		systemAuthenticator.begin("admin");


		List<Customer> allCustomers = dataManager.load(Customer.class).all().list();

		dataManager.remove(allCustomers);
	}

	@AfterEach
	void tearDown() {
		systemAuthenticator.end();
	}


	@Nested
	class PropertyConditionBehaviour {

		@Nested
		class ThoseTestsShouldFail {
			@Test
			void given_homerCustomerInDb_when_searchingForCustomerWithEmptyName_then_homerIsFound_whichIsWrong() {

				// given:
				Customer homer = saveCustomer("Homer Simpson");

				// when:
				Optional<Customer> foundCustomer = dataManager.load(Customer.class)
						.condition(PropertyCondition.equal("name", ""))
						.optional();

				// then:
				assertThat(foundCustomer)
						.isPresent()
						.get()
						.isEqualTo(homer);
			}

		}

		@Nested
		class ThoseTestsShouldPass {

			@Test
			void given_homerCustomerInDb_when_searchingForCustomerWithEmptyName_then_marioIsFound() {

				// given:
				saveCustomer("Homer Simpson");

				// when:
				Optional<Customer> foundCustomer = dataManager.load(Customer.class)
						.condition(PropertyCondition.equal("name", ""))
						.optional();

				// then:
				assertThat(foundCustomer)
						.isNotPresent();
			}

			@Test
			void given_homerCustomerInDb_when_searchingForCustomerWithNullName_then_marioIsFound() {

				// given:
				saveCustomer("Homer Simpson");


				// when:
				Optional<Customer> foundCustomer = dataManager.load(Customer.class)
						.condition(PropertyCondition.equal("name", null))
						.optional();

				// then:
				assertThat(foundCustomer)
						.isNotPresent();
			}
		}
	}
	@Nested
	class QueryBehaviour {

		@Nested
		class ThoseTestsShouldFail {
			@Test
			void given_homerCustomerInDb_when_searchingForCustomerWithEmptyName_then_homerIsFound_whichIsWrong() {

				// given:
				Customer homer = saveCustomer("Homer Simpson");

				// when:
				Optional<Customer> foundCustomer = dataManager.load(Customer.class)
						.query("select c from Customer c where c.name = :name")
						.parameter("name", "")
						.optional();

				// then: the customer is actually found. This is totally wrong...
				assertThat(foundCustomer)
						.isPresent()
						.get()
						.isEqualTo(homer);
			}
		}

		@Nested
		class ThoseTestsShouldPass {

			@Test
			void given_homerCustomerInDb_when_searchingForCustomerWithEmptyName_then_marioIsFound() {

				// given:
				saveCustomer("Homer Simpson");

				// when:
				Optional<Customer> foundCustomer = dataManager.load(Customer.class)
						.query("select c from Customer c where c.name = :name")
						.parameter("name", "")
						.optional();

				// then:
				assertThat(foundCustomer)
						.isNotPresent();
			}


			@Test
			void given_homerCustomerInDb_when_searchingForCustomerWithNullName_then_marioIsFound() {

				// given:
				saveCustomer("Homer Simpson");


				// when:
				Optional<Customer> foundCustomer = dataManager.load(Customer.class)
						.query("select c from Customer c where c.name = :name")
						.parameter("name", null)
						.optional();

				// then:
				assertThat(foundCustomer)
						.isNotPresent();
			}
		}
	}


	private Customer saveCustomer(String name) {
		Customer customer = dataManager.create(Customer.class);
		customer.setName(name);
		return dataManager.save(customer);
	}

}
