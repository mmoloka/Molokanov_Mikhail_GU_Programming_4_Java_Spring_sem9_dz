package org.example.api;

import com.github.javafaker.Faker;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneId;
import java.util.*;

@RestController
@RequestMapping("/api/issue")
public class IssueController {
    private final List<Issue> issues;
    private final BookProvider bookProvider;
    private final ReaderProvider readerProvider;
    private final Faker faker;

    public IssueController(BookProvider bookProvider, ReaderProvider readerProvider) {
        this.issues = new ArrayList<>();
        this.bookProvider = bookProvider;
        this.readerProvider = readerProvider;
        this.faker = new Faker();
    }

    private Date startOfYear() {
        Calendar instance = Calendar.getInstance();
        instance.set(Calendar.YEAR, 2024);
        instance.set(Calendar.MONTH, Calendar.JANUARY);
        instance.set(Calendar.DAY_OF_MONTH, 1);
        return instance.getTime();
    }

    private Date endOfYear() {
        Calendar instance = Calendar.getInstance();
        instance.set(Calendar.YEAR, 2024);
        instance.set(Calendar.MONTH, Calendar.DECEMBER);
        instance.set(Calendar.DAY_OF_MONTH, 31);
        return instance.getTime();
    }

    @GetMapping
    public List<Issue> getAll() {
        return issues;
    }

    @GetMapping("/random")
    public Issue getRandom() {
        final int randomIndex = faker.number().numberBetween(0, issues.size());
        return issues.get(randomIndex);
    }

    @GetMapping("/refresh")
    public List<Issue> refresh() {
        refreshData();
        return issues;
    }

    private void refreshData() {
        issues.clear();
        for (int i = 0; i < 15; i++) {
            Issue issue = new Issue();
            issue.setId(UUID.randomUUID());
            issue.setIssuedAt(
                    faker.date().between(startOfYear(), endOfYear()).toInstant()
                            .atZone(ZoneId.systemDefault()).toLocalDate());
            issue.setBook(bookProvider.getRandomBook());
            issue.setReader(readerProvider.getRandomReader());
            issues.add(issue);
        }


    }
}
