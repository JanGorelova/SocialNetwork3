package com.exam.logic.actions;

import com.exam.logic.Action;
import com.exam.logic.services.ProfileService;
import com.exam.logic.services.Validator;
import com.exam.models.Profile;
import com.exam.models.User;
import com.exam.util.NameNormalizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

import static com.exam.logic.Constants.*;

public class ProfileEditPostAction implements Action {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        User currentUser = (User) request.getSession().getAttribute(CURRENT_USER);
        String telephone = request.getParameter("telephone");
        String birthday = request.getParameter("birthday");
        String country = request.getParameter("country");
        String city = request.getParameter("city");
        String university = request.getParameter("university");
        String position = request.getParameter("position");
        String about = request.getParameter("about");
        Validator.ValidCode validCode = Validator.validateProfile(telephone, birthday, country, city, university, about);
        if (validCode != Validator.ValidCode.SUCCESS) {
            request.setAttribute(ERROR_MSG, validCode.getPropertyName());
            Profile.ProfileBuilder profileBuilder = Profile.builder()
                    .id(currentUser.getId())
                    .country(country)
                    .city(city)
                    .position(position)
                    .telephone(telephone)
                    .about(about);
            try {
                profileBuilder.birthday(LocalDate.parse(birthday));
            } catch (Exception ignored) {
            }
            request.setAttribute(PROFILE, profileBuilder.build());
        } else {
            LocalDate date;
            date = LocalDate.parse(birthday);
            Profile profile = Profile.builder()
                    .id(currentUser.getId())
                    .telephone(telephone.isEmpty() ? null : telephone)
                    .birthday(date)
                    .country(country.isEmpty() ? null : NameNormalizer.multiNormalize(country))
                    .city(city.isEmpty() ? null : NameNormalizer.multiNormalize(city))
                    .university(university.isEmpty() ? null : university)
                    .position(position.isEmpty() ? null : position)
                    .about(about.isEmpty() ? null : about)
                    .build();
            ProfileService profileService = (ProfileService) request.getServletContext().getAttribute(PROFILE_SERVICE);
            profileService.update(profile);
            request.setAttribute(PROFILE, profile);
            request.setAttribute(SUCCESS_MSG, validCode.getPropertyName());
        }
        return "/WEB-INF/jsp/profile/edit.jsp";
    }
}