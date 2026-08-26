package com.rtravez.msa.service;

import com.rtravez.msa.entity.view.CustomerView;
import com.rtravez.msa.repository.ICustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <b> Description de la clase, interface o enumeration. </b>
 *
 * @author renetravez
 * @version $1.0$
 */
@Service
@Slf4j
public class CustomerService extends GenericService<CustomerView, Long, ICustomerRepository> implements ICustomerService {

    protected CustomerService(ICustomerRepository repository) {
        super(repository);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<CustomerView> customer = this.findCustomerByUsername(username);

        if (customer.isEmpty()) {
            log.error("Error en el Login: no existe el usuario '{}' en el sistema!", username);
            throw new UsernameNotFoundException("Username: " + username + " no existe en el sistema!");
        }

        List<SimpleGrantedAuthority> authorities = customer.get().getRoleCustomers().stream().map(role -> new SimpleGrantedAuthority(role.getRole().getName())).collect(Collectors.toList());
        authorities.forEach(authority -> log.info("Role: ".concat(authority.getAuthority())));

        if (authorities.isEmpty()) {
            log.error("Error en el Login: Usuario {} no tiene roles asignados!", username);
            throw new UsernameNotFoundException("Error en el Login: usuario " + username + " no tiene roles asignados!");
        }
        return new User(customer.get().getUsername(), customer.get().getPassword(), customer.get().getStatus(), true, true, true, authorities);
    }

    @Override
    public Optional<CustomerView> findCustomerByUsername(String username) {
        return repository.findCustomerByUsername(username);
    }


}
