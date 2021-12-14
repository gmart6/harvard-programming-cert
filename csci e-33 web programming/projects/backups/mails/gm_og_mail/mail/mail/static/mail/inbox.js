document.addEventListener('DOMContentLoaded', function() {

  // Use buttons to toggle between views
  document.querySelector('#inbox').addEventListener('click', () => location.reload());
  document.querySelector('#sent').addEventListener('click', () => load_mailbox('sent'));
  document.querySelector('#archived').addEventListener('click', () => load_mailbox('archive'));
  document.querySelector('#compose').addEventListener('click', compose_email);

  // By default, load the inbox
  load_mailbox('inbox');
});

function compose_email() {

  // Show compose view and hide other views
  document.querySelector('#emails-view').style.display = 'none';
  document.querySelector('#read-view').style.display = 'none';
  document.querySelector('#compose-view').style.display = 'block';

  // Show compose title, hide reply title
  document.querySelector("#response").style.display = "none";
  document.querySelector("#new").style.display = "initial";

  // Clear out composition fields
  document.querySelector('#compose-recipients').value = '';
  document.querySelector('#compose-subject').value = '';
  document.querySelector('#compose-body').value = '';

  document.querySelector("#compose-form").onsubmit = () => {

    const compRecips = document.querySelector('#compose-recipients').value;
    const compSubj = document.querySelector('#compose-subject').value;
    const compBody = document.querySelector('#compose-body').value;

    fetch('/emails', {
      method: 'POST',
      body: JSON.stringify({
          recipients: compRecips,
          subject: compSubj,
          // Had a hard time with formatting this portion, pre tags seemed to kinda solve it, though it gets cut
          body: `<pre> ${compBody} </pre>`
      })
    })
    .then(response => response.json())
    .then(result => {
      // Print result
      console.log(result);

      // Check for an error
      const errorChk = result["error"];
      if (errorChk == undefined) {
        load_mailbox('sent');
      } else if (errorChk == "At least one recipient required.") {
        alert("At least one recipient is required.");
      } else {
        alert("Couldn't find that recipient. Please ensure your recipient has an account.")
      }

    });

    return false;

  }
}

function reply_email(id) {

  fetch(`/emails/${id}`)
  .then(response => response.json())
  .then(email => {
  // Use compose form
  compose_email();

  // Show reply title, hide compose title
  document.querySelector("#response").style.display = "initial";
  document.querySelector("#new").style.display = "none";

  /* I originially set this, as per the specification, to the sender of the 
    original email, but wouldn't it make more sense  to have a conditional to
     always send to the counterpart, as opposed to one's self, if they're the
     original sender? */
  if (email.sender == document.querySelector("#self").value) {
    document.querySelector('#compose-recipients').value = email.recipients;
  } else {
    document.querySelector('#compose-recipients').value = email.sender;
  }

  // Check if reply subject line is formatted properly
  if (email.subject.startsWith("Re:")) {
    document.querySelector('#compose-subject').value = email.subject;
  } else {
    document.querySelector('#compose-subject').value = `Re: ${email.subject}`;
  }
  
  document.querySelector('#compose-body').value = `\n \n ----- \n \n On ${email.timestamp}, ${email.sender} wrote: ${email.body}`;
  
  });

}


function load_mailbox(mailbox) {
  
  // Show the mailbox and hide other views
  document.querySelector('#emails-view').style.display = 'block';
  document.querySelector('#read-view').style.display = 'none';
  document.querySelector('#compose-view').style.display = 'none';

  // Show the mailbox name
  document.querySelector('#emails-view').innerHTML = `<h3>${mailbox.charAt(0).toUpperCase() + mailbox.slice(1)}</h3>`;

  fetch(`/emails/${mailbox}`)
  .then(response => response.json())
  .then(emails => {
      // Print emails
      console.log(emails);
  
      // Run function to display each email
      emails.forEach(display_email);
  });

}

function read_email(id) {

  // Show the read view and hide other views
  document.querySelector('#emails-view').style.display = 'none';
  document.querySelector('#read-view').style.display = 'block';
  document.querySelector('#compose-view').style.display = 'none';

  // Clear out email detail fields
  document.querySelector('#email-details').innerHTML = '';

  fetch(`/emails/${id}`)
  .then(response => response.json())
  .then(email => {
    // Print email
    console.log(email);

    // Populate email detail fields
    // I couldn't find a more elegant way to do this (less repetition) because of the changing references for each item
    const from = document.createElement('div');
    from.innerHTML = `<strong>From: </strong> ${email.sender}`;
    document.querySelector('#email-details').append(from);

    const recips = document.createElement('div');
    recips.innerHTML = `<strong>To: </strong> ${email.recipients}`;
    document.querySelector('#email-details').append(recips);

    const subj = document.createElement('div');
    subj.innerHTML = `<strong>Subject: </strong> ${email.subject}`;
    document.querySelector('#email-details').append(subj);

    const tmstmp = document.createElement('div');
    tmstmp.innerHTML = `<strong>Timestamp: </strong> ${email.timestamp}`;
    document.querySelector('#email-details').append(tmstmp);

    const emailBody = document.createElement('div');
    emailBody.innerHTML = `<hr> ${email.body}`;
    document.querySelector('#email-details').append(emailBody);

    /* Determine whether to show archive or unarchive buttons, not really seeing
       a great way to not show archive buttons on messages coming out of sent box,
       as you can self-reply to a message and send to yourself; */
    if (email.sender == document.querySelector("#self").value) {
      document.querySelector("#unarchive").style.display = "none";
      document.querySelector("#archive").style.display = "none";
    } else if (email.archived === false) {
      document.querySelector("#unarchive").style.display = "none";
      document.querySelector("#archive").style.display = "initial";
    } else {
      document.querySelector("#archive").style.display = "none";
      document.querySelector("#unarchive").style.display = "initial";
    }

  })

  // Mark the email read
  fetch(`/emails/${id}`, {
    method: 'PUT',
    body: JSON.stringify({
        read: true
    })
  })

  // Archive the email
  document.querySelector("#archive").addEventListener("click", function() {
    fetch(`/emails/${id}`, {
      method: 'PUT',
      body: JSON.stringify({
        archived: true
      })
    })
    location.reload();
  })

  // Unarchive the email
  document.querySelector("#unarchive").addEventListener("click", function() {
    fetch(`/emails/${id}`, {
      method: 'PUT',
      body: JSON.stringify({
        archived: false
      })
    })
    location.reload();
  })

  // Mark the email unread
  document.querySelector("#unread").addEventListener("click", function() {
    fetch(`/emails/${id}`, {
      method: 'PUT',
      body: JSON.stringify({
        read: false
      })
    })
    location.reload();
  })

  // Reply to the email
  document.querySelector("#reply").addEventListener("click", () => reply_email(id));
  
}

function display_email(emails) {
  const email_element = document.createElement('div');

  // Apply a class to the div, populate it
  email_element.classList.add('mailbox-email');
  
  console.log(emails);
  email_element.innerHTML = ` <strong>${emails.sender}</strong> ${emails.subject}  <span class="timestamped">${emails.timestamp}</span>`;
  console.log(email_element);

  if (emails.read === true) {
    email_element.style.backgroundColor = "Gainsboro";
  }
  
  // Upon email being clicked, open it
  email_element.addEventListener('click', function() {
    console.log('This element has been clicked!')
    read_email(emails.id);
  });
  console.log(email_element);

  document.querySelector('#emails-view').append(email_element);

}
