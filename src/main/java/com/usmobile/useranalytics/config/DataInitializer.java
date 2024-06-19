package com.usmobile.useranalytics.config;

import com.usmobile.useranalytics.model.Cycle;
import com.usmobile.useranalytics.model.DailyUsage;
import com.usmobile.useranalytics.model.User;
import com.usmobile.useranalytics.repository.CycleRepository;
import com.usmobile.useranalytics.repository.DailyUsageRepository;
import com.usmobile.useranalytics.repository.UserRepository;
import com.usmobile.useranalytics.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CycleRepository cycleRepository;
    private final DailyUsageRepository dailyUsageRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(UserRepository userRepository, CycleRepository cycleRepository, DailyUsageRepository dailyUsageRepository) {
        this.userRepository = userRepository;
        this.cycleRepository = cycleRepository;
        this.dailyUsageRepository = dailyUsageRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public void run(String... args) throws Exception {
        initializeData();
    }

    public void initializeData() {
        userRepository.deleteAll();
        cycleRepository.deleteAll();
        dailyUsageRepository.deleteAll();

        // Create users
        User user1 = new User("12345", "Parwaz", "Gill", "parwazgill3@gmail.com", passwordEncoder.encode("parwaz123"));
        User user2 = new User("67890", "Arnaaz", "Gill", "arnaazgill@gmail.com", passwordEncoder.encode("arnaaz123"));
        userRepository.saveAll(List.of(user1, user2));

        // Create cycles
        Date startDate1 = DateUtils.getDateDaysAgoMidnight(33);
        Date endDate1 = DateUtils.getDateDaysAgoEndOfDay(4);
        Date startDate2 = DateUtils.getDateDaysAgoMidnight(3);
        Date endDate2 = DateUtils.getDateDaysFromNowEndOfDay(26);
        Date startDate3 = DateUtils.getDateDaysAgoMidnight(29);
        Date endDate3 = DateUtils.getDateDaysFromNowEndOfDay(0);
        Cycle cycle1 = new Cycle("cycle1", "2027050505", startDate1, endDate1, "12345");
        Cycle cycle2 = new Cycle("cycle2", "2027050505", startDate2, endDate2, "12345");
        Cycle cycle3 = new Cycle("cycle3", "3037050505", startDate3, endDate3, "67890");
        cycleRepository.saveAll(List.of(cycle1, cycle2, cycle3));

        // Create daily usages
        DailyUsage usage1 = new DailyUsage("usage1", "2027050505", "12345", "cycle1", DateUtils.getDateDaysAgoMidnight(6), 100.0);
        DailyUsage usage2 = new DailyUsage("usage2", "2027050505", "12345", "cycle1", DateUtils.getDateDaysAgoMidnight(5), 200.0);
        DailyUsage usage3 = new DailyUsage("usage3", "2027050505", "12345", "cycle1", DateUtils.getDateDaysAgoMidnight(4), 300.0);
        DailyUsage usage4 = new DailyUsage("usage4", "2027050505", "12345", "cycle2", DateUtils.getDateDaysAgoMidnight(2), 100.0);
        DailyUsage usage5 = new DailyUsage("usage5", "2027050505", "12345", "cycle2", DateUtils.getDateDaysAgoMidnight(1), 200.0);
        DailyUsage usage6 = new DailyUsage("usage6", "2027050505", "12345", "cycle2", DateUtils.getTodayMidnight(), 300.0);
        dailyUsageRepository.saveAll(List.of(usage1, usage2, usage3, usage4, usage5, usage6));
    }
}
