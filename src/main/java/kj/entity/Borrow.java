package kj.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Borrow {

    int id;
    Student student;
    Book book;

    public Borrow(Student student, Book book) {
        this.student = student;
        this.book = book;
    }
}
