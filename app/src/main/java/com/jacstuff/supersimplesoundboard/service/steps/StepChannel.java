package com.jacstuff.supersimplesoundboard.service.steps;

import com.jacstuff.supersimplesoundboard.service.SoundHolder;

public record StepChannel(int index, SoundHolder soundHolder, boolean isEnabled, boolean isMuted) {
}
