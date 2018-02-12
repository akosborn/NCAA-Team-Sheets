package me.andrewosborn.persistence;

import me.andrewosborn.model.Conference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConferenceService
{
    private ConferenceRepository conferenceRepository;

    @Autowired
    public ConferenceService(ConferenceRepository conferenceRepository)
    {
        this.conferenceRepository = conferenceRepository;
    }

    public List<Conference> saveAll(List<Conference> conferences)
    {
        return conferenceRepository.save(conferences);
    }
}
