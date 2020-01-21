$(document).ready(function() {
  $('.slideshow-images').slick({
    dots: true,
    arrows: true,
    centerMode: true,
    autoplay: true,
    autoplaySpeed: 4000,
    speed: 1000,
    centerPadding: '0px',
    responsive: [
      {
        breakpoint: 480,
        settings: {
          arrows: false,
          centerPadding: '40px',
        }
      }
    ]
  });
});