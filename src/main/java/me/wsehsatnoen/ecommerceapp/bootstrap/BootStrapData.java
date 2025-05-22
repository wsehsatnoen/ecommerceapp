package me.wsehsatnoen.ecommerceapp.bootstrap;

import me.wsehsatnoen.ecommerceapp.dao.CustomerRepository;
import me.wsehsatnoen.ecommerceapp.dao.DivisionRepository;
import me.wsehsatnoen.ecommerceapp.entities.Customer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BootStrapData implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final DivisionRepository divisionRepository;

    public BootStrapData(CustomerRepository customerRepository, DivisionRepository divisionRepository) {
        this.customerRepository = customerRepository;
        this.divisionRepository = divisionRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        Customer baileyPope = new Customer("Bailey", "Pope", "123 Memory Lane", "78729", "3617171888", divisionRepository.getReferenceById(42L));
        Customer josephVigil = new Customer("Joseph", "Vigil", "335 Hated Way", "32988", "7478883939", divisionRepository.getReferenceById(42L));
        Customer stephenRenard = new Customer("Stephen", "Renard", "477 No Way Street", "47333", "8887774444", divisionRepository.getReferenceById(42L));
        Customer justinJones = new Customer("Justin", "Jones", "578 Sleepy Ave", "74783", "7478883939", divisionRepository.getReferenceById(42L));
        Customer miloCasselman = new Customer("Milo", "Casselman", "747 Over It Tr", "757646", "7774448488", divisionRepository.getReferenceById(42L));

        customerRepository.save(baileyPope);
        customerRepository.save(josephVigil);
        customerRepository.save(stephenRenard);
        customerRepository.save(justinJones);
        customerRepository.save(miloCasselman);

    }
}
