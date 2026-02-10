/**
 * Form Validation - KISS Version
 */
(function() {
    'use strict';
    
    document.addEventListener('DOMContentLoaded', function() {
        const form = document.querySelector('.needs-validation');
        if (!form) return;
        
        form.addEventListener('submit', function(event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        }, false);
    });
})();

function resetForm() {
    // Reset form fields
    const form = document.querySelector('.needs-validation');
    if (form) {
        form.reset();
        form.classList.remove('was-validated');
    }
    
    // Hide result div if it exists
    const resultDiv = document.getElementById('resultDiv');
    if (resultDiv) {
        resultDiv.style.display = 'none';
    }
    
    // Clear any error and success alerts
    const errorAlerts = document.querySelectorAll('.alert-danger');
    const successAlerts = document.querySelectorAll('.alert-success');
    errorAlerts.forEach(alert => alert.remove());
    successAlerts.forEach(alert => alert.remove());
}
