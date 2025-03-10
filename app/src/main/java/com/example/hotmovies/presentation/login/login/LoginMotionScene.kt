package com.example.hotmovies.presentation.login.login

import androidx.compose.runtime.Composable
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionScene

@OptIn(ExperimentalMotionApi::class)
@Composable
fun LoginMotionScene() = MotionScene(
    """ 
{
  Transitions: {
    default: {
      from: 'start',
      to: 'end',
      pathMotionArc: 'startHorizontal',
      KeyFrames: {
        KeyPositions: [
          {
            target: ['username'],
            frames: [60],
            percentWidth: [1],
            percentHeight: [0.1],
            type: 'pathRelative'
          },
          {
            target: ['password'],
            frames: [30, 95],
            percentWidth: [0.8, 1],
            percentHeight: [0.6, 1],
            type: 'pathRelative'
          }, 
          {
            target: ['login'],
            frames: [80],
            percentWidth: [0.7],
            percentHeight: [0.7],
            type: 'pathRelative'
          },
        ],
        KeyAttributes: [
          {
            target: ['indicator'],
            frames: [0, 98, 100],
            alpha: [0, 0, 1],
          }
        ],
      },
    },
  },
  ConstraintSets: {
    start: {
      guideline: { type: 'hGuideline', percent: 0.48 },
      appTitle: {
        start: ['parent', 'start', 20],
        end: ['parent', 'end', 20],
        bottom: ['description', 'top', 5],
        width: 'wrap',
        height: 'wrap',
      },
      description: {
        start: ['parent', 'start', 30],
        end: ['parent', 'end', 30],
        bottom: ['credits', 'top', 30],
        width: 'preferWrap',
        height: 'wrap',
      },
      credits: {
        start: ['parent', 'start', 30],
        end: ['parent', 'end', 30],
        bottom: ['login', 'top', 10],
        width: 'preferWrap',
        height: 'wrap',
      },
      fireStart: {
        width: 40,
        height: 90,
        bottom: ['username', 'top', -25],
        start: ['username', 'start', -14],
      },
      fireEnd: {
        width: 40,
        height: 90,
        bottom: ['username', 'top', -25],
        start: ['username', 'end', -26],
      },
      username: {
        start: ['parent', 'start', 20],
        end: ['parent', 'end', 20],
        bottom: ['password', 'top', 40],
        width: { value: '63%', min: 200, max: 760 },
        height: 'wrap',
      },
      password: {
        bottom: ['guideline', 'top', 30],
        start: ['parent', 'start', 20],
        end: ['parent', 'end', 20],
        width: { value: '63%', min: 200, max: 760 },
        height: 'wrap',
      },
      login: {
        top: ['guideline', 'top'],
        vBias: 0.6,
        start: ['parent', 'start', 20],
        end: ['parent', 'end', 20],
        bottom: ['parent', 'bottom'],
        width: { value: '26%', min: 80, max: 500 },
        height: 'wrap',
      }, 
      indicator: {
        start: ['login', 'start'],
        end: ['login', 'end'],
        bottom: ['login', 'bottom'],
        top: ['login', 'top'],
        width: 30,
        height: 30,
        alpha: 0,
      },
    },
    end: {
      guideline: { type: 'hGuideline', percent: 0.48 },
      appTitle: {
        start: ['parent', 'start', 20],
        end: ['parent', 'end', 20],
        bottom: ['description', 'top', 5],
        width: 'wrap',
        height: 'wrap',
      },
      description: {
        start: ['parent', 'start', 30],
        end: ['parent', 'end', 30],
        bottom: ['credits', 'top', 30],
        width: 'preferWrap',
        height: 'wrap',
      },
      credits: {
        start: ['parent', 'start', 30],
        end: ['parent', 'end', 30],
        bottom: ['login', 'top', 10],
        width: 'preferWrap',
        height: 'wrap',
      },
      fireStart: {
        width: 40,
        height: 90,
        bottom: ['username', 'top', -25],
        start: ['username', 'start', -14],
      },
      fireEnd: {
        width: 40,
        height: 90,
        bottom: ['username', 'top', -25],
        start: ['username', 'end', -26],
      },
      username: {
        start: ['login', 'start'],
        end: ['login', 'end'],
        top: ['login', 'top'],
        bottom: ['login', 'bottom'],
        width: 75,
        height: 75,
      },
      password: {
        start: ['login', 'start'],
        end: ['login', 'end'],
        top: ['login', 'top'],
        bottom: ['login', 'bottom'],
        width: 60,
        height: 75,
      },
      login: {
        top: ['guideline', 'top'],
        vBias: 0.7,
        start: ['parent', 'start', 20],
        end: ['parent', 'end', 20],
        bottom: ['parent', 'bottom'],
        width: 85,
        height: 85,
      },
      indicator: {
        start: ['login', 'start'],
        end: ['login', 'end'],
        bottom: ['login', 'bottom'],
        top: ['login', 'top'],
        width: 30,
        height: 30,
        alpha: 1,
      },
    },
  },
}
"""
)

