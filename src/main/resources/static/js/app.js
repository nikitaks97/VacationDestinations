// Modern Interactive Features for Vacation Destinations

document.addEventListener('DOMContentLoaded', function() {
    // Add existing initializations
    initializeThemeSelector();
    initializeAnimations();
    initializeParallaxEffects();
    initializeWeatherWidget();
    initializeLikeAnimations();
    initializeImageLazyLoading();
    
    // Add new enhanced features
    initializeScrollEffects();
    initializeFormEnhancements();
    initializeImageEnhancements();
    initializeSearchEnhancements();
    initializeAccessibilityFeatures();
    initializePerformanceOptimizations();
});

// Theme Selector
function initializeThemeSelector() {
    const themes = {
        tropical: {
            primary: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)',
            secondary: 'linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)',
            background: 'linear-gradient(-45deg, #00c6ff, #0072ff, #00ff88, #00d4aa)'
        },
        sunset: {
            primary: 'linear-gradient(135deg, #fa709a 0%, #fee140 100%)',
            secondary: 'linear-gradient(135deg, #ff9a9e 0%, #fecfef 100%)',
            background: 'linear-gradient(-45deg, #ff9a9e, #fecfef, #ffecd2, #fcb69f)'
        },
        purple: {
            primary: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
            secondary: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)',
            background: 'linear-gradient(-45deg, #667eea, #764ba2, #f093fb, #f5576c)'
        },
        pink: {
            primary: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)',
            secondary: 'linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%)',
            background: 'linear-gradient(-45deg, #f093fb, #f5576c, #ffecd2, #fcb69f)'
        }
    };

    // Create theme selector if it doesn't exist
    if (!document.querySelector('.theme-selector')) {
        const themeSelector = document.createElement('div');
        themeSelector.className = 'theme-selector';
        themeSelector.innerHTML = `
            <div class="theme-btn theme-tropical" data-theme="tropical" title="Tropical Theme"></div>
            <div class="theme-btn theme-sunset" data-theme="sunset" title="Sunset Theme"></div>
            <div class="theme-btn theme-purple" data-theme="purple" title="Purple Theme"></div>
            <div class="theme-btn theme-pink" data-theme="pink" title="Pink Theme"></div>
        `;
        document.body.appendChild(themeSelector);
    }

    // Add click handlers
    document.querySelectorAll('.theme-btn').forEach(btn => {
        btn.addEventListener('click', function() {
            const themeName = this.dataset.theme;
            applyTheme(themes[themeName]);
            
            // Add selection indicator
            document.querySelectorAll('.theme-btn').forEach(b => b.classList.remove('active'));
            this.classList.add('active');
        });
    });
}

function applyTheme(theme) {
    const root = document.documentElement;
    root.style.setProperty('--primary-gradient', theme.primary);
    root.style.setProperty('--secondary-gradient', theme.secondary);
    document.body.style.background = theme.background;
    document.body.style.backgroundSize = '400% 400%';
}

// Enhanced Animations
function initializeAnimations() {
    // Stagger animation for cards
    const cards = document.querySelectorAll('.destination-card');
    cards.forEach((card, index) => {
        card.style.animationDelay = `${index * 0.1}s`;
        card.classList.add('fade-in-up');
    });

    // Add hover sound effects (optional)
    cards.forEach(card => {
        card.addEventListener('mouseenter', function() {
            this.style.transform = 'translateY(-15px) scale(1.02) rotateX(5deg)';
        });
        
        card.addEventListener('mouseleave', function() {
            this.style.transform = 'translateY(0) scale(1) rotateX(0deg)';
        });
    });
}

// Parallax Effects
function initializeParallaxEffects() {
    const parallaxElements = document.querySelectorAll('.parallax-image');
    
    window.addEventListener('scroll', function() {
        const scrollTop = window.pageYOffset;
        
        parallaxElements.forEach(element => {
            const speed = 0.5;
            const yPos = -(scrollTop * speed);
            element.style.transform = `translateY(${yPos}px)`;
        });
    });
}

// Weather Widget
function initializeWeatherWidget() {
    const destinations = document.querySelectorAll('.destination-card');
    
    destinations.forEach(card => {
        const weatherWidget = document.createElement('div');
        weatherWidget.className = 'weather-widget position-absolute';
        weatherWidget.style.cssText = 'top: 10px; right: 10px; z-index: 10;';
        
        // Simulate weather data
        const weather = getRandomWeather();
        weatherWidget.innerHTML = `
            <div style="font-size: 0.8rem;">
                <div>${weather.temp}°C</div>
                <div>${weather.icon}</div>
            </div>
        `;
        
        const cardBody = card.querySelector('.card-img-top').parentElement;
        cardBody.style.position = 'relative';
        cardBody.appendChild(weatherWidget);
    });
}

function getRandomWeather() {
    const weathers = [
        { temp: 28, icon: '☀️' },
        { temp: 22, icon: '⛅' },
        { temp: 25, icon: '🌤️' },
        { temp: 30, icon: '🌞' },
        { temp: 18, icon: '🌧️' }
    ];
    return weathers[Math.floor(Math.random() * weathers.length)];
}

// Enhanced Like Animations
function initializeLikeAnimations() {
    const likeButtons = document.querySelectorAll('.like-btn, button[type="submit"]');
    
    likeButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            // Create floating hearts
            createFloatingHearts(e.target);
            
            // Add ripple effect
            const ripple = document.createElement('span');
            ripple.className = 'ripple';
            ripple.style.cssText = `
                position: absolute;
                border-radius: 50%;
                background: rgba(255, 255, 255, 0.6);
                transform: scale(0);
                animation: ripple 0.6s linear;
                pointer-events: none;
            `;
            
            const rect = this.getBoundingClientRect();
            const size = Math.max(rect.width, rect.height);
            ripple.style.width = ripple.style.height = size + 'px';
            ripple.style.left = (e.clientX - rect.left - size / 2) + 'px';
            ripple.style.top = (e.clientY - rect.top - size / 2) + 'px';
            
            this.style.position = 'relative';
            this.style.overflow = 'hidden';
            this.appendChild(ripple);
            
            setTimeout(() => ripple.remove(), 600);
        });
    });
}

function createFloatingHearts(element) {
    for (let i = 0; i < 5; i++) {
        const heart = document.createElement('div');
        heart.innerHTML = '💖';
        heart.style.cssText = `
            position: fixed;
            pointer-events: none;
            z-index: 10000;
            font-size: ${Math.random() * 20 + 15}px;
            left: ${element.getBoundingClientRect().left + Math.random() * 100}px;
            top: ${element.getBoundingClientRect().top}px;
            animation: floatHeart 2s ease-out forwards;
        `;
        
        document.body.appendChild(heart);
        setTimeout(() => heart.remove(), 2000);
    }
}

// Image Lazy Loading with Animation
function initializeImageLazyLoading() {
    const images = document.querySelectorAll('img[data-src]');
    
    const imageObserver = new IntersectionObserver((entries, observer) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                const img = entry.target;
                img.src = img.dataset.src;
                img.classList.add('fade-in');
                observer.unobserve(img);
            }
        });
    });
    
    images.forEach(img => imageObserver.observe(img));
}

// Scroll Effects
function initializeScrollEffects() {
    // Create intersection observer for scroll animations
    const observerOptions = {
        threshold: 0.1,
        rootMargin: '0px 0px -50px 0px'
    };

    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('visible');
            }
        });
    }, observerOptions);

    // Observe all animation elements
    document.querySelectorAll('.fade-in-up, .fade-in-left, .fade-in-right').forEach(el => {
        observer.observe(el);
    });

    // Enhanced navbar scroll effect
    initializeNavbarScrollEffect();
}

function initializeNavbarScrollEffect() {
    const navbar = document.querySelector('.navbar');
    if (!navbar) return;

    let lastScrollTop = 0;
    
    window.addEventListener('scroll', () => {
        const scrollTop = window.pageYOffset || document.documentElement.scrollTop;
        
        if (scrollTop > 100) {
            navbar.classList.add('scrolled');
        } else {
            navbar.classList.remove('scrolled');
        }

        // Hide navbar on scroll down, show on scroll up
        if (scrollTop > lastScrollTop && scrollTop > 200) {
            navbar.style.transform = 'translateY(-100%)';
        } else {
            navbar.style.transform = 'translateY(0)';
        }
        
        lastScrollTop = scrollTop <= 0 ? 0 : scrollTop;
    }, false);
}

// Enhanced form interactions
function initializeFormEnhancements() {
    const forms = document.querySelectorAll('form');
    
    forms.forEach(form => {
        // Add loading state to submit buttons
        form.addEventListener('submit', function(e) {
            const submitBtn = form.querySelector('button[type="submit"]');
            if (submitBtn) {
                submitBtn.classList.add('btn-loading');
                submitBtn.disabled = true;
                
                // Re-enable after 3 seconds (in case of validation errors)
                setTimeout(() => {
                    submitBtn.classList.remove('btn-loading');
                    submitBtn.disabled = false;
                }, 3000);
            }
        });

        // Enhanced input animations
        const inputs = form.querySelectorAll('input, textarea');
        inputs.forEach(input => {
            input.addEventListener('focus', function() {
                this.parentElement.classList.add('focused');
            });
            
            input.addEventListener('blur', function() {
                this.parentElement.classList.remove('focused');
            });
        });
    });
}

// Enhanced image interactions
function initializeImageEnhancements() {
    const images = document.querySelectorAll('.destination-card img, .card-img-top');
    
    images.forEach(img => {
        // Add loading placeholder
        img.addEventListener('load', function() {
            this.classList.add('loaded');
        });

        // Add error handling
        img.addEventListener('error', function() {
            this.src = 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNDAwIiBoZWlnaHQ9IjMwMCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KICA8cmVjdCB3aWR0aD0iNDAwIiBoZWlnaHQ9IjMwMCIgZmlsbD0iI2Y4ZjlmYSIvPgogIDx0ZXh0IHg9IjUwJSIgeT0iNTAlIiBmb250LWZhbWlseT0iQXJpYWwiIGZvbnQtc2l6ZT0iMTgiIGZpbGw9IiM2Yzc1N2QiIHRleHQtYW5jaG9yPSJtaWRkbGUiIGR5PSIuM2VtIj5JbWFnZSBub3QgZm91bmQ8L3RleHQ+Cjwvc3ZnPg==';
            this.alt = 'Image not available';
        });

        // Add click to enlarge functionality
        img.addEventListener('click', function() {
            createImageModal(this.src, this.alt);
        });
    });
}

function createImageModal(src, alt) {
    const modal = document.createElement('div');
    modal.className = 'image-modal';
    modal.style.cssText = `
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: rgba(0, 0, 0, 0.9);
        z-index: 9999;
        display: flex;
        align-items: center;
        justify-content: center;
        cursor: pointer;
        backdrop-filter: blur(10px);
    `;

    const img = document.createElement('img');
    img.src = src;
    img.alt = alt;
    img.style.cssText = `
        max-width: 90%;
        max-height: 90%;
        object-fit: contain;
        border-radius: 15px;
        box-shadow: 0 20px 60px rgba(0, 0, 0, 0.5);
    `;

    modal.appendChild(img);
    document.body.appendChild(modal);

    // Close on click
    modal.addEventListener('click', () => {
        document.body.removeChild(modal);
    });

    // Close on Escape key
    document.addEventListener('keydown', function(e) {
        if (e.key === 'Escape' && document.querySelector('.image-modal')) {
            document.body.removeChild(modal);
        }
    });
}

// Enhanced search functionality
function initializeSearchEnhancements() {
    const searchInputs = document.querySelectorAll('input[type="search"], input[placeholder*="search"]');
    
    searchInputs.forEach(input => {
        let searchTimeout;
        
        input.addEventListener('input', function() {
            clearTimeout(searchTimeout);
            searchTimeout = setTimeout(() => {
                performSearch(this.value);
            }, 300);
        });
    });
}

function performSearch(query) {
    const cards = document.querySelectorAll('.destination-card');
    
    cards.forEach(card => {
        const title = card.querySelector('.card-title')?.textContent.toLowerCase() || '';
        const description = card.querySelector('.card-text')?.textContent.toLowerCase() || '';
        const searchTerm = query.toLowerCase();
        
        if (title.includes(searchTerm) || description.includes(searchTerm) || query === '') {
            card.style.display = 'block';
            card.classList.add('fade-in-up');
        } else {
            card.style.display = 'none';
        }
    });
}

// Enhanced accessibility features
function initializeAccessibilityFeatures() {
    // Add keyboard navigation for cards
    const cards = document.querySelectorAll('.destination-card');
    
    cards.forEach((card, index) => {
        card.setAttribute('tabindex', '0');
        card.setAttribute('role', 'button');
        card.setAttribute('aria-label', `View destination: ${card.querySelector('.card-title')?.textContent || 'Unknown'}`);
        
        card.addEventListener('keydown', function(e) {
            if (e.key === 'Enter' || e.key === ' ') {
                e.preventDefault();
                const link = this.querySelector('a');
                if (link) link.click();
            }
        });
    });

    // Add focus indicators
    const focusableElements = document.querySelectorAll('a, button, input, textarea, select, [tabindex]:not([tabindex="-1"])');
    
    focusableElements.forEach(el => {
        el.addEventListener('focus', function() {
            this.classList.add('focused');
        });
        
        el.addEventListener('blur', function() {
            this.classList.remove('focused');
        });
    });
}

// Performance optimizations
function initializePerformanceOptimizations() {
    // Lazy load images
    if ('IntersectionObserver' in window) {
        const imageObserver = new IntersectionObserver((entries, observer) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    const img = entry.target;
                    img.src = img.dataset.src;
                    img.classList.remove('lazy');
                    imageObserver.unobserve(img);
                }
            });
        });

        document.querySelectorAll('img[data-src]').forEach(img => {
            imageObserver.observe(img);
        });
    }

    // Debounce scroll events
    let scrollTimeout;
    window.addEventListener('scroll', () => {
        if (!scrollTimeout) {
            scrollTimeout = setTimeout(() => {
                scrollTimeout = null;
                // Perform scroll-based operations here
            }, 16); // ~60fps
        }
    });
}

// Add CSS animations dynamically
const style = document.createElement('style');
style.textContent = `
    @keyframes ripple {
        to {
            transform: scale(2);
            opacity: 0;
        }
    }
    
    @keyframes floatHeart {
        0% {
            opacity: 1;
            transform: translateY(0) rotate(0deg);
        }
        100% {
            opacity: 0;
            transform: translateY(-100px) rotate(180deg);
        }
    }
    
    @keyframes fade-in-up {
        from {
            opacity: 0;
            transform: translateY(30px);
        }
        to {
            opacity: 1;
            transform: translateY(0);
        }
    }
    
    .fade-in-up {
        animation: fade-in-up 0.6s ease forwards;
    }
    
    .fade-in {
        animation: fadeIn 0.5s ease;
    }
    
    @keyframes fadeIn {
        from { opacity: 0; }
        to { opacity: 1; }
    }
    
    .theme-btn.active {
        transform: scale(1.2);
        box-shadow: 0 0 20px rgba(255, 255, 255, 0.8);
    }
    
    .animate {
        animation: bounceIn 0.6s ease;
    }
    
    @keyframes bounceIn {
        0% {
            opacity: 0;
            transform: scale(0.3) translateY(20px);
        }
        50% {
            opacity: 1;
            transform: scale(1.05);
        }
        70% {
            transform: scale(0.9);
        }
        100% {
            opacity: 1;
            transform: scale(1) translateY(0);
        }
    }

    /* Scroll-based animations */
    .fade-in-up {
        opacity: 0;
        transform: translateY(20px);
        transition: opacity 0.6s ease, transform 0.6s ease;
    }
    
    .fade-in-left {
        opacity: 0;
        transform: translateX(-20px);
        transition: opacity 0.6s ease, transform 0.6s ease;
    }
    
    .fade-in-right {
        opacity: 0;
        transform: translateX(20px);
        transition: opacity 0.6s ease, transform 0.6s ease;
    }
    
    .visible {
        opacity: 1;
        transform: translateY(0);
    }
    
    .navbar.scrolled {
        background: rgba(26, 32, 53, 0.9);
        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.3);
    }
    
    .btn-loading {
        position: relative;
        pointer-events: none;
    }
    
    .btn-loading:after {
        content: '';
        position: absolute;
        top: 50%;
        left: 50%;
        width: 20px;
        height: 20px;
        border: 3px solid rgba(255, 255, 255, 0.7);
        border-top-color: transparent;
        border-radius: 50%;
        animation: spin 0.6s linear infinite;
        transform: translate(-50%, -50%);
    }
    
    @keyframes spin {
        0% { transform: translate(-50%, -50%) rotate(0deg); }
        100% { transform: translate(-50%, -50%) rotate(360deg); }
    }
    
    /* Image modal styles */
    .image-modal {
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: rgba(0, 0, 0, 0.9);
        z-index: 9999;
        display: flex;
        align-items: center;
        justify-content: center;
        cursor: pointer;
        backdrop-filter: blur(10px);
    }
    
    .image-modal img {
        max-width: 90%;
        max-height: 90%;
        object-fit: contain;
        border-radius: 15px;
        box-shadow: 0 20px 60px rgba(0, 0, 0, 0.5);
    }
`;
document.head.appendChild(style);

// Particle background effect
function createParticleBackground() {
    const canvas = document.createElement('canvas');
    canvas.style.cssText = `
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        pointer-events: none;
        z-index: -1;
        opacity: 0.3;
    `;
    document.body.appendChild(canvas);
    
    const ctx = canvas.getContext('2d');
    canvas.width = window.innerWidth;
    canvas.height = window.innerHeight;
    
    const particles = [];
    const particleCount = 50;
    
    for (let i = 0; i < particleCount; i++) {
        particles.push({
            x: Math.random() * canvas.width,
            y: Math.random() * canvas.height,
            size: Math.random() * 3 + 1,
            speedX: (Math.random() - 0.5) * 2,
            speedY: (Math.random() - 0.5) * 2,
            color: `hsl(${Math.random() * 360}, 70%, 80%)`
        });
    }
    
    function animateParticles() {
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        
        particles.forEach(particle => {
            particle.x += particle.speedX;
            particle.y += particle.speedY;
            
            if (particle.x < 0 || particle.x > canvas.width) particle.speedX *= -1;
            if (particle.y < 0 || particle.y > canvas.height) particle.speedY *= -1;
            
            ctx.beginPath();
            ctx.arc(particle.x, particle.y, particle.size, 0, Math.PI * 2);
            ctx.fillStyle = particle.color;
            ctx.fill();
        });
        
        requestAnimationFrame(animateParticles);
    }
    
    animateParticles();
    
    window.addEventListener('resize', () => {
        canvas.width = window.innerWidth;
        canvas.height = window.innerHeight;
    });
}

// Initialize particle background
setTimeout(createParticleBackground, 1000);

// Add smooth page transitions
function initializePageTransitions() {
    const links = document.querySelectorAll('a[href^="/"]');
    
    links.forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            
            // Add fade out effect
            document.body.style.opacity = '0.7';
            document.body.style.transform = 'scale(0.98)';
            document.body.style.transition = 'all 0.3s ease';
            
            setTimeout(() => {
                window.location.href = this.href;
            }, 300);
        });
    });
}

// Add error handling for all features
window.addEventListener('error', function(e) {
    console.error('An error occurred:', e.error);
    // Optionally show user-friendly error message
});

// Add Service Worker for offline functionality (optional)
if ('serviceWorker' in navigator) {
    window.addEventListener('load', function() {
        navigator.serviceWorker.register('/sw.js')
            .then(function(registration) {
                console.log('ServiceWorker registration successful');
            })
            .catch(function(err) {
                console.log('ServiceWorker registration failed');
            });
    });
}
