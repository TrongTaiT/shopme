package com.shopme.admin.brand;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.shopme.common.entity.Brand;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class BrandServiceTests {
	
	@MockBean
	private BrandRepository repo;
	
	@InjectMocks
	private BrandService service;
	
	@Test
	public void testCheckUniqueInNewModeReturnFalse() {
		Integer id = null;
		String name = "Acer";
		Brand brand = new Brand(name);
		
		Mockito.when(repo.findByName(name)).thenReturn(brand);
		
		boolean isUnique = service.isNameUnique(id, name);
		assertTrue(isUnique == false);
	}
	
	@Test
	public void testCheckUniqueInNewModeReturnTrue() {
		Integer id = null;
		String name = "AMD";
		
		Mockito.when(repo.findByName(name)).thenReturn(null);
		
		boolean isUnique = service.isNameUnique(id, name);
		assertTrue(isUnique == true);		
	}
	
	@Test
	public void testCheckUniqueInEditModeReturnFalse() {
		Integer id = 3;
		String name = "AMD";
		Brand brand = new Brand(id, name);
		
		Mockito.when(repo.findByName(name)).thenReturn(brand);
		
		boolean isUnique = service.isNameUnique(1, name);
		assertTrue(isUnique == false);		
	}
	
	@Test
	public void testCheckUniqueInEditModeReturnTrue() {
		Integer id = 3;
		String name = "AMD";
		Brand brand = new Brand(id, name);
		
		Mockito.when(repo.findByName(name)).thenReturn(brand);
		
		boolean isUnique = service.isNameUnique(id, name);
		assertTrue(isUnique == true);		
	}

}
