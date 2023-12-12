package com.example.todoapp.repositories;

import com.example.todoapp.models.ListCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListCategoryRepository extends JpaRepository<ListCategory, Long> {

}
