package in.softops.springsecurity.db;

import in.softops.springsecurity.model.Customer;
import in.softops.springsecurity.model.Employee;
import in.softops.springsecurity.model.Role;
import in.softops.springsecurity.repository.CustomerRepository;
import in.softops.springsecurity.repository.EmployeeRepository;
import in.softops.springsecurity.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    public DataInitializer(
            CustomerRepository customerRepository,
            EmployeeRepository employeeRepository,
            RoleRepository roleRepository, PasswordEncoder encoder) {
        this.customerRepository = customerRepository;
        this.employeeRepository = employeeRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) throws Exception {

        if (roleRepository.count() <= 0 && customerRepository.count() <= 0
                && employeeRepository.count() <= 0) {
            Role role1 = new Role();
            role1.setName("EMPLOYEE");
            Role role2 = new Role();
            role2.setName("CUSTOMER");
            roleRepository.saveAll(Arrays.asList(role1, role2));

            Set<Role> employeeRoles = new HashSet<>();
            Set<Role> customerRoles = new HashSet<>();
            Role employeeRole = roleRepository.findByName("EMPLOYEE")
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            Role customerRole = roleRepository.findByName("CUSTOMER")
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            employeeRoles.add(employeeRole);
            customerRoles.add(customerRole);

            Customer customer = new Customer();
            customer.setUsername("staff");
            customer.setPassword(encoder.encode("staff@123"));
            customer.setEnabled(true);
            customer.setRoles(customerRoles);
            customerRepository.save(customer);

            Employee employee = new Employee();
            employee.setUsername("employee");
            employee.setPassword(encoder.encode("employee@123"));
            employee.setEnabled(true);
            employee.setRoles(employeeRoles);
            employeeRepository.save(employee);
        }
    }
}
