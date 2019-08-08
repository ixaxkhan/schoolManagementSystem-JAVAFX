<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Smsfield extends CI_Controller {

	public function smsarea() {
		$this->load->view('/admin/smsform');
	}

	public function sendsmsbydept() {
		//
		include 'application/SMS/WebSMS.php';
		$dept = $this->input->post('department');
		$users = $this->db->get_where('user', array('department' => $dept));
		$message = $this->input->post('message');
		foreach ($users->result() as $user) {
			$number = $user->mnumber;
			WebSMS::setApp('0dae24be-9635-46de-9953-5aa1cd73e2f3', 'MTRkNDYyMmQtNWFlOS00YmNmLTg1MDYtNGNkYzEwNGMwMzcw')
				->send($number, $message);
		}
		$data['success'] = "<p class='alert alert-success'>Message send successfully</p";
		$this->load->view('admin/sms_to_department', $data);

	}
	public function sendsmsbydeptandprog() {
		include 'application/SMS/WebSMS.php';
		$dept = $this->input->post('dept');
		$prog = $this->input->post('prog');
		$users = $this->db->get_where('user', array('department' => $dept, 'programe' => $prog));
		$message = $this->input->post('message');
		foreach ($users->result() as $user) {
			$number = $user->mnumber;
			WebSMS::setApp('0dae24be-9635-46de-9953-5aa1cd73e2f3', 'MTRkNDYyMmQtNWFlOS00YmNmLTg1MDYtNGNkYzEwNGMwMzcw')
				->send($number, $message);
		}

		$data['success'] = "<p class='alert alert-success'>Message send successfully</p";
		$this->load->view('admin/sms_to_programe', $data);

	}

	public function sendsms() {
		include 'application/SMS/WebSMS.php';
		// $users = $this->load->model('Model File');
		// $users = $this->db->get('user');
		// return $users
		// $dept= $this->input->post('dept');
		// $prog= $this->input->post('prog');
		// $query = $this->db->get_where('user', array('department' => $dept,'program' => $prog));
		$users = $this->db->get('user');
		$message = $this->input->post('message');
		foreach ($users->result() as $user) {
			$number = $user->mnumber;
			WebSMS::setApp('0dae24be-9635-46de-9953-5aa1cd73e2f3', 'MTRkNDYyMmQtNWFlOS00YmNmLTg1MDYtNGNkYzEwNGMwMzcw')
				->send($number, $message);

		}
        $data['success'] = "<p class='alert alert-success'>Message send successfully</p>";
        $this->load->view('admin/smsform', $data);
	}

	public function sendsmsbydesignation() {
        include 'application/SMS/WebSMS.php';
		//Sending Message to the student and parent
		$desig = $this->input->post('designation');

		$users = $this->db->get_where('user', array('designation' => $desig));
		$message = $this->input->post('message');
		foreach ($users->result() as $user) {
			$number = $user->mnumber;
			WebSMS::setApp('0dae24be-9635-46de-9953-5aa1cd73e2f3', 'MTRkNDYyMmQtNWFlOS00YmNmLTg1MDYtNGNkYzEwNGMwMzcw')
				->send($number, $message);
		}

		$data['success'] = "<p class='alert alert-success'>Message send successfully</p";
		$this->load->view('admin/sms_to_designation', $data);

	}

	public function smstodepartment() {

		$this->load->view('admin/sms_to_department');

	}

	public function smstoprograme() {

		$this->load->view('admin/sms_to_programe');
	}

	public function smstodesignation() {

		$this->load->view('admin/sms_to_designation');
	}

}

/* End of file smsfield.php */
/* Location: ./application/controllers/smsfield.php */