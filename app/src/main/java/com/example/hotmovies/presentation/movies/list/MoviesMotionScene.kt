package com.example.hotmovies.presentation.movies.list

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionScene

@Composable
@OptIn(ExperimentalMotionApi::class, ExperimentalMaterial3Api::class)
fun MoviesMotionScene(compactCollapsibleHeader: Boolean, actionBarHeightDp: Float = TopAppBarDefaults.MediumAppBarCollapsedHeight.value) =
    MotionScene(
        """ 
{
  Transitions: {
    default: {
      from: 'start',
      to: 'end',
      KeyFrames: {
        KeyAttributes: [
          {
            target: ['avatar'],
            frames: [0, 15, 30, 40, 100],
            translationY: [0, -30, 20, 20, 20],
            translationX: [0, -74, -20, 0, 0],
            translationZ: [0, 0, -1, -1, -1],
            rotationZ: [0, 45, 4, 0, 0],
            alpha: [1, 1, 1, 1, 0],
          },
        ],
      },
    },
  },
  ConstraintSets: {
    start: {
      background: {
        start: ['parent', 'start', 30],
        end: ['parent', 'end'],
        top: ['parent', 'top'],
        width: 'spread',
        height: ${ if(compactCollapsibleHeader) 0 else 200},
        translationZ: -2,
      },
      indicator: {
        start: ['avatar', 'start'],
        end: ['avatar', 'end'],
        bottom: ['avatar', 'bottom'],
        top: ['avatar', 'top'],
        width: 31,
        height: 31,
      },         
      avatar: {
        start: ['info', 'start', 20],
        top: ['info', 'top', ${ if(compactCollapsibleHeader) 0 else -20}],
        width: 100,
        height: 100,
        translationZ: 0,
        alpha: 1,
      }, 
      info: {
        start: ['parent', 'start'],
        end: ['parent', 'end'],
        top: ['parent', 'top', ${if(compactCollapsibleHeader) 0 else 110}],
        width: 'spread',
        height: 150
      },
       name: {
        start: ['avatar', 'end', 10],
        end: ['info', 'end', 10],
        top: ['info', 'top'],
        width: 'spread',
        height: 'wrap'
      }, 
       username: {
        start: ['name', 'start'],
        end: ['info', 'end', 10],
        top: ['name', 'bottom', -3],
        width: 'spread',
        height: 'wrap',
        alpha: 1,
      },
       description: {
        start: ['avatar', 'start'],
        end: ['info', 'end',10],
        top: ['avatar', 'bottom', 6],
        width: 'spread',
        height: 'wrap',
        alpha: 1,
      },
      moviesGrid: {
        start: ['parent', 'start'],
        end: ['parent', 'end'],
        bottom: ['parent', 'bottom'],
        top: ['info', 'bottom'],
        width: 'spread',
        height: 'spread',
      }   
    },
    end: {
      background: {
        start: ['parent', 'start'],
        end: ['parent', 'end'],
        top: ['parent', 'top'],
        width: 'spread',
        height: ${if(compactCollapsibleHeader) 0 else actionBarHeightDp},
        translationZ: -2,
      },
      indicator: {
        start: ['avatar', 'start'],
        end: ['avatar', 'end'],
        bottom: ['avatar', 'bottom'],
        top: ['avatar', 'top'],
        width: 30,
        height: 30,
      },       
      avatar: {
        start: ['info', 'start', 20],
        top: ['info', 'top', -20],
        width: 100,
        height: 100,
        translationZ: -1,
        alpha: 0,
      },      
      info: {
        start: ['parent', 'start'],
        end: ['parent', 'end'],
        top: ['parent', 'top'],
        width: 'spread',
        height: $actionBarHeightDp
      },
       name: {
        start: ['info', 'start', 10],
        end: ['info', 'end', 10],
        top: ['info', 'top'],
        width: 'spread',
        height: 'wrap',
      },
       username: {
        start: ['info', 'start'],
        end: ['info', 'end', 10],
        top: ['name', 'bottom', -3],
        width: 'spread',
        height: 'wrap',
        alpha: 0,
      },
       description: {
        start: ['info', 'start'],
        end: ['info', 'end', 10],
        top: ['username', 'bottom', 6],
        width: 'spread',
        height: 'wrap',
        alpha: 0,
      },
      moviesGrid: {
        start: ['parent', 'start'],
        end: ['parent', 'end'],
        bottom: ['parent', 'bottom'],
        top: ['info', 'bottom'],
        width: 'spread',
        height: 'spread',
      }       
    },
  },
}
"""
    )