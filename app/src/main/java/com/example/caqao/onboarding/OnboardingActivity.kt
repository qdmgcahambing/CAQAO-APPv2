package com.example.caqao.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.caqao.MainActivity2
import com.example.caqao.R

private lateinit var onboardingItemsAdapter: OnboardingItemsAdapter
private lateinit var indicatorsContainer: LinearLayout

class OnboardingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
        setOnboardingItems()
        setupIndicators()
        setupCurrentIndicator(0)

        supportActionBar?.hide()
    }

    private fun setOnboardingItems() {
        onboardingItemsAdapter = OnboardingItemsAdapter(
            listOf(
                OnboardingItem(
                    onboardingImage = R.drawable.efficient,
                    title = "EFFICIENT",
                    description = "Experience ease of use and fast \n" +
                            "cut test quality assessment of your\n" +
                            "cacao beans"
                ),
                OnboardingItem(
                    onboardingImage = R.drawable.accurate,
                    title = "ACCURATE",
                    description = "Accurate classifications of cut\n" +
                            "test cacao beans to ensure the\n" +
                            "best quality"
                ),
                OnboardingItem(
                    onboardingImage = R.drawable.main_feature,
                    title = "MAIN FEATURES",
                    description = "Capture or Upload a picture of your cut \n" +
                            "test cacao beans in a guillotine, enter the\n" +
                            "bean size, then click assess"
                )
            )
        )
        val onboardingViewPager = findViewById<ViewPager2>(R.id.onboarding_bg)
        onboardingViewPager.adapter = onboardingItemsAdapter
        onboardingViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setupCurrentIndicator(position)
            }
        })
        (onboardingViewPager.getChildAt(0) as RecyclerView).overScrollMode =
            RecyclerView.OVER_SCROLL_NEVER

        findViewById<AppCompatButton>(R.id.next_button).setOnClickListener {
            if (onboardingViewPager.currentItem + 1  < onboardingItemsAdapter.itemCount) {
                onboardingViewPager.currentItem += 1
            } else {
                navigateToMainActivity2()
            }
        }

        findViewById<TextView>(R.id.skip_txt).setOnClickListener {
            navigateToMainActivity2()
        }

        findViewById<AppCompatButton>(R.id.getstarted_button).setOnClickListener {
            navigateToMainActivity2()
        }

    }

    private fun navigateToMainActivity2() {
        startActivity(Intent(applicationContext, MainActivity2::class.java))
        finish()
    }

    private fun setupIndicators() {
        indicatorsContainer = findViewById(R.id.indicatorsContainer)
        val indicators = arrayOfNulls<ImageView>(onboardingItemsAdapter.itemCount)
        val layoutParams: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        layoutParams.setMargins(8,0,8,0)
        for (i in indicators.indices) {
            indicators[i] = ImageView(applicationContext)
            indicators[i]?.let {
                it.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_inactive_bg
                    )
                )
                it.layoutParams = layoutParams
                indicatorsContainer.addView(it)
            }
        }
    }

    private fun setupCurrentIndicator(position: Int) {
        val childCount = indicatorsContainer.childCount
        for (i in 0 until childCount) {
            val imageView = indicatorsContainer.getChildAt(i) as ImageView
            if (i == position) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_active_bg
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_inactive_bg
                    )
                )
            }
        }
    }

}